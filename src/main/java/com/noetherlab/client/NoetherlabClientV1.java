package com.noetherlab.client;

import com.google.common.base.Joiner;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noetherlab.client.io.csv.EdgarIO;
import com.noetherlab.client.io.csv.SecurityIO;
import com.noetherlab.client.io.csv.SortedDataFrameIO;
import com.noetherlab.client.io.csv.SubmissionsIO;
import com.noetherlab.client.io.json.LocalDateTimeAdapter;
import com.noetherlab.client.io.json.LocalTimeAdapter;
import com.noetherlab.client.model.GeomBrownian;
import com.noetherlab.client.model.SECSecurity;
import com.noetherlab.client.model.Security;
import com.noetherlab.client.model.Submission;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.ws.rs.core.MediaType;
import okhttp3.*;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.protocol.RedirectStrategy;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.http.ssl.TLS;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.pool.PoolReusePolicy;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class NoetherlabClientV1 {

    private static final String ENDPOINT = "http://api.noetherlab.com";
    private static final Integer TIMEOUT = 10;
    private static final String TOKEN_HEADER = "TOKEN";
    private final String apiKey;

    public static final Logger logger = LoggerFactory.getLogger(NoetherlabClientV1.class);

    private CloseableHttpClient client;

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setPrettyPrinting().create();


    public NoetherlabClientV1(String apiKey) {
        this.apiKey = apiKey;
    }

    private CloseableHttpClient getHttpClient() {
        if(client == null) {

            int timeout = 10;

            PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(
                            SSLConnectionSocketFactoryBuilder.create()
                                    .setSslContext(SSLContexts.createSystemDefault())
                                    .setTlsVersions(TLS.V_1_3, TLS.V_1_2)
                                    .build())
                    .setDefaultSocketConfig(SocketConfig.custom()
                            .setSoTimeout(Timeout.ofSeconds(timeout))
                            .setSoKeepAlive(true)
                            .build())
                    .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.STRICT)
                    .setConnPoolPolicy(PoolReusePolicy.LIFO)
                    .build();


            RequestConfig config = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeout, TimeUnit.SECONDS)
                    .setResponseTimeout(timeout, TimeUnit.SECONDS)
                    .build();


            Collection<Header> headers = new ArrayList<>();
            headers.add(new BasicHeader("TOKEN", apiKey));


            RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            HttpRequestRetryStrategy retryStrategy =
                    new DefaultHttpRequestRetryStrategy(3, TimeValue.ofSeconds(timeout));

            client = HttpClients.custom()
                    .setConnectionManager(connectionManager)
                    .setDefaultRequestConfig(config)
                    .setDefaultHeaders(headers)
                    .setRedirectStrategy(redirectStrategy)
                    .evictExpiredConnections()
                    .evictIdleConnections(Timeout.ofSeconds(timeout))
                    .setRetryStrategy(retryStrategy)
                    .build();
        }
        return client;
    }


    OkHttpClient httpClient;

    private OkHttpClient getClient() {
        if(httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(3L * TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }
        return httpClient;
    }


    public String getVersion() {

        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("version");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .build();

           Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            return body.string();
        } catch (Exception e) {
            logger.error("Cannot retrieve version", e);
            return null;
        }
    }


    /*
     * Index API
     */

    public Map<String, String> listIndex() {

        try {
            URIBuilder builder = new URIBuilder(ENDPOINT).setPathSegments("v1", "index");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }
            String responseString = body.string();
            okhttp3.MediaType contentTypeMediaType = body.contentType();
            if(contentTypeMediaType == null) {
                throw new Exception("Unknown content type");
            }
            String contentType = contentTypeMediaType.toString();


            Map<String, String> list = new LinkedHashMap<>();
            if(contentType.equals("text/csv")) {
                CSVReader reader = new CSVReaderBuilder(new StringReader(responseString))
                        .withSkipLines(1)
                        .build();

                String[] record;
                while ((record = reader.readNext()) != null) {
                    list.put(record[0], record[1]);
                }
            }

            return list;

        } catch (Exception e) {
            logger.error("Cannot list indexes", e);
            return null;
        }
    }

    public TreeSet<LocalDate> getIndexTimeline(String index) {

        try {
            URIBuilder builder = new URIBuilder(ENDPOINT).setPathSegments("v1", "index", index, "timeline");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }
            String responseString = body.string();
            okhttp3.MediaType contentTypeMediaType = body.contentType();
            if(contentTypeMediaType == null) {
                throw new Exception("Unknown content type");
            }
            String contentType = contentTypeMediaType.toString();

            TreeSet<LocalDate> list = new TreeSet<>();
            if(contentType.equals("text/csv")) {
                CSVReader reader = new CSVReaderBuilder(new StringReader(responseString))
                        .withSkipLines(1)
                        .build();

                String[] record;
                while ((record = reader.readNext()) != null) {
                    list.add(LocalDate.parse(record[0], DateTimeFormatter.ISO_DATE));
                }
            }
            return list;
        } catch (Exception e) {
            logger.error("Cannot retrieve index timeline for " + index, e);
            return null;
        }
    }


    public Map<String, Float> getLastIndexComposition(String index) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT).setPathSegments("v1", "index", index, "composition");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }
            String responseString = body.string();
            okhttp3.MediaType contentTypeMediaType = body.contentType();
            if(contentTypeMediaType == null) {
                throw new Exception("Unknown content type");
            }
            String contentType = contentTypeMediaType.toString();

            Map<String, Float> composition = new LinkedHashMap<>();
            if(contentType.equals("text/csv")) {
                CSVReader reader = new CSVReaderBuilder(new StringReader(responseString))
                        .withSkipLines(1)
                        .build();

                String[] record;
                while ((record = reader.readNext()) != null) {
                    composition.put(record[0],Float.parseFloat(record[1]));
                }
            }
            return composition;
        } catch (Exception e) {
            logger.error("CCannot retrieve index composition for " + index, e);
            return null;
        }
    }

    /*
     * Security API
     */

    public Collection<Security> getSecurities(SecuritiesQuery query) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT).setPathSegments("v1", "securities");

            if(query.getSecurityIds() != null && !query.getSecurityIds().isEmpty()) {
                builder.setParameter("security_ids", Joiner.on(",").join(query.getSecurityIds()));
            }
            if(query.getTags() != null && !query.getTags().isEmpty()) {
                builder.setParameter("tags", Joiner.on(",").join(query.getTags()));
            }
            if(query.getIndices() != null && !query.getIndices().isEmpty()) {
                builder.setParameter("indices", Joiner.on(",").join(query.getIndices()));
            }

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }
            String responseString = body.string();
            okhttp3.MediaType contentTypeMediaType = body.contentType();
            if(contentTypeMediaType == null) {
                throw new Exception("Unknown content type");
            }
            String contentType = contentTypeMediaType.toString();

            Collection<Security> composition = new ArrayList<>();
            if(contentType.equals("text/csv")) {
                composition = SecurityIO.fromCSV(responseString);
            }
            return composition;
        } catch (Exception e) {
            logger.error("CCannot retrieve securities", e);
            return null;
        }
    }

    /// TODO


    /*
     * Price API
     */

    public SortedDataFrame<LocalDate, String, Float> getPrice(Security security) {
        return getPrice(security, true);
    }

    public SortedDataFrame<LocalDate, String, Float> getPrice(Security security, boolean withMeta) {
        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);

            //////
            InputStream isMeta = null;
            if(withMeta) {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("security", security.getId(), "prices", "meta");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isMeta = response.getEntity().getContent();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("security", security.getId(), "prices", "data");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isData = response.getEntity().getContent();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);
            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public SortedDataFrame<LocalDate, String, Float> getStatement(Security security) {
        return getStatement(security, true);
    }

    public SortedDataFrame<LocalDate, String, Float> getStatement(Security security, boolean withMeta) {

        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);


            InputStream isMeta = null;
            if(withMeta) {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments(security.getId(), "statements", "meta");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isMeta = response.getEntity().getContent();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments(security.getId(), "statements", "data");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isData = response.getEntity().getContent();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);

            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public SortedDataFrame<LocalDate, String, Float> getStdStatement(Security security) {
        return getStdStatement(security, true);
    }

    public SortedDataFrame<LocalDate, String, Float> getStdStatement(Security security, boolean withMeta) {
        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);

            //////
            InputStream isMeta = null;
            if(withMeta) {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments(security.getId(), "statements-std", "meta");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isMeta = response.getEntity().getContent();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments(security.getId(), "statements-std", "data");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isData = response.getEntity().getContent();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);

            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }


    public Map<String, GeomBrownian> getModels() {
        return getModels(null);
    }


    public SortedDataFrame<LocalDate, String, Float> getCurrency(String currency) {
        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("currency", currency,  "prices", "data");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isData = response.getEntity().getContent();
            }

            SortedDataFrameIO.readCSV(res, null, isData);
            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }


    public SortedDataFrame<LocalDate, String, Float> getEnvironment() {

        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);

            //////
            InputStream isMeta;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("environment", "meta");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isMeta = response.getEntity().getContent();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("environment", "data");

                HttpGet request = new HttpGet(builder.build());

                CloseableHttpResponse response = getHttpClient().execute(request);

                if (response.getCode() != HttpStatus.SC_OK) {
                    String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new Exception(responseString);
                }
                isData = response.getEntity().getContent();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);

            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }

    }

    public Map<String, GeomBrownian> getModels(Collection<Security> securities) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("models");

            if(securities != null && !securities.isEmpty()) {
                builder.addParameter("security_ids", Joiner.on(",").join(securities.stream().map(Security::getId).toList()));
            }
            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Accept", MediaType.APPLICATION_JSON);

            CloseableHttpResponse response = getHttpClient().execute(request);
            String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (response.getCode() != HttpStatus.SC_OK) {
                throw new Exception(responseString);
            }

            Type type = new TypeToken<Map<String, GeomBrownian>>(){}.getType();
            return gson.fromJson(responseString, type);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Collection<Submission> getSubmissions(String secId) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments(secId, "submissions");


            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Accept", "text/csv");

            CloseableHttpResponse response = getHttpClient().execute(request);
            String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (response.getCode() != HttpStatus.SC_OK) {
                throw new Exception(responseString);
            }

            return SubmissionsIO.submissionsFromCSV(responseString);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public byte[] getSubmission(String secId, String accessionNumber) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments(secId, accessionNumber);


            HttpGet request = new HttpGet(builder.build());

            request.setHeader("Accept", MediaType.APPLICATION_OCTET_STREAM);

            CloseableHttpResponse response = getHttpClient().execute(request);

            if(response.getCode() == HttpStatus.SC_NO_CONTENT) {
                return null;
            }

            if (response.getCode() != HttpStatus.SC_OK) {
                String res = EntityUtils.toString(response.getEntity());
                throw new Exception(res);
            }

            return EntityUtils.toByteArray(response.getEntity());
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Map<String, String> getSubmissionContent(String secId, String accessionNumber) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments(secId, accessionNumber, "content");


            HttpGet request = new HttpGet(builder.build());

            CloseableHttpResponse response = getHttpClient().execute(request);

            if (response.getCode() == HttpStatus.SC_NO_CONTENT) {
                return null;
            }

            String res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (response.getCode() != HttpStatus.SC_OK) {
                throw new Exception(res);
            }

            Type type = new TypeToken<Map<String, String>>(){}.getType();


            return gson.fromJson(res, type);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Collection<SECSecurity> getEdgarSecurities(Security s) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments(s.getId(), "securities-std");

            HttpGet request = new HttpGet(builder.build());
            request.setHeader("Accept", "text/csv");

            CloseableHttpResponse response = getHttpClient().execute(request);
            String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            if (response.getCode() != HttpStatus.SC_OK) {
                throw new Exception(responseString);
            }

            return EdgarIO.securitiesFromCSV(responseString);
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }


}
