package com.noetherlab.client;

public class ClientTest {

    NoetherlabClientV1 client;

    public void initClient() {
        client = new NoetherlabClientV1(System.getenv("NOETHERLAB_API_KEY"));
    }

}
