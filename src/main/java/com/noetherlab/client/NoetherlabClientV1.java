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
import com.noetherlab.client.model.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import jakarta.ws.rs.core.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Type;
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

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .setPrettyPrinting().create();


    public NoetherlabClientV1(String apiKey) {
        this.apiKey = apiKey;
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
                        .setPathSegments("v1", "security", security.getId(), "prices", "meta");

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

                isMeta = body.byteStream();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("v1", "security", security.getId(), "prices", "data");

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

                isData = body.byteStream();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);
            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public SortedDataFrame<LocalDate, String, Float> getCurrency(String currency) {
        try {
            SortedDataFrame<LocalDate, String, Float> res =
                    new SortedDataFrame<>(LocalDate.class, String.class, Float.class);

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("v1", "currency", currency,  "prices", "data");

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

                isData = body.byteStream();
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
                        .setPathSegments("v1", "environment", "meta");

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

                isMeta = body.byteStream();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("v1", "environment", "data");

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

                isData = body.byteStream();
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
                        .setPathSegments("v1", security.getId(), "statements", "meta");

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

                isMeta = body.byteStream();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("v1", security.getId(), "statements", "data");

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

                isData = body.byteStream();
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
                        .setPathSegments("v1", security.getId(), "statements-std", "meta");

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

                isMeta = body.byteStream();
            }

            //////
            InputStream isData;
            {
                URIBuilder builder = new URIBuilder(ENDPOINT)
                        .setPathSegments("v1", security.getId(), "statements-std", "data");

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

                isData = body.byteStream();
            }

            SortedDataFrameIO.readCSV(res, isMeta, isData);

            return res;
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }



    public Collection<Submission> getSubmissions(String secId) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", secId, "submissions");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .header("Accept", "text/csv")
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            return SubmissionsIO.submissionsFromCSV(body.string());

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }


    public Collection<Announcement> getAnnouncements(String secId) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", secId, "announcements");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .header("Accept", "text/csv")
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            return SubmissionsIO.announcementsFromCSV(body.string());

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public byte[] getSubmission(String secId, String accessionNumber) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", secId, accessionNumber);


            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .header("Accept",  MediaType.APPLICATION_OCTET_STREAM)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }

            if(response.code() == HttpStatus.SC_NO_CONTENT) {
                return null;
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            return body.bytes();
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Collection<SECSecurity> getEdgarSecurities(Security s) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", s.getId(), "securities-std");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .header("Accept", "text/csv")
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            return EdgarIO.securitiesFromCSV(body.string());

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Map<String, String> getSubmissionContent(String secId, String accessionNumber) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", secId, accessionNumber, "content");

            Request request = new Request.Builder()
                    .get().url(builder.build().toURL())
                    .header(TOKEN_HEADER, apiKey)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }

            if(response.code() == HttpStatus.SC_NO_CONTENT) {
                return null;
            }
            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            Type type = new TypeToken<Map<String, String>>(){}.getType();
            return gson.fromJson(body.string(), type);

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public Map<String, GeomBrownian> getModels(SecuritiesQuery query) {
        try {
            URIBuilder builder = new URIBuilder(ENDPOINT)
                    .setPathSegments("v1", "models");

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
                    .header("Accept", MediaType.APPLICATION_JSON)
                    .build();

            Response response = getClient().newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new Exception(response.code() + ": " + response.message());
            }

            ResponseBody body = response.body();
            if (body == null) {
                throw new Exception("Body is empty");
            }

            Type type = new TypeToken<Map<String, GeomBrownian>>(){}.getType();
            return gson.fromJson(body.string(), type);

        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }


}
