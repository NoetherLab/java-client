package com.noetherlab.client;


import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VersionTest extends ClientTest {

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    public void getVersion() {
        //Arrange

        //Act
        String version = client.getVersion();

        //Assert
        assertNotNull(version);
        String[] chunks = version.split("\\.");
        assertEquals(chunks.length, 3);
        for(String chunk : chunks) {
            assertTrue(StringUtils.isNumeric(chunk));
        }
    }
}
