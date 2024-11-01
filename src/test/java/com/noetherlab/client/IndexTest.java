package com.noetherlab.client;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IndexTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    public void list() {
        //Arrange

        //Act
        Map<String, String> indices = client.listIndex();

        //Assert
        assertNotNull(indices);
        assertFalse(indices.isEmpty());
        assertTrue(indices.size() > 5);
    }




}
