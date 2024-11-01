package com.noetherlab.client;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Index API Test")
public class IndexTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("List indices")
    public void list() {
        //Arrange

        //Act
        Map<String, String> indices = client.listIndex();

        //Assert
        assertNotNull(indices);
        assertFalse(indices.isEmpty());
        assertTrue(indices.size() > 5);
    }


    @Test
    @Order(2)
    @DisplayName("List timeline for SP500")
    public void timelineSP500() {
        //Arrange
        String index = "^GSPC";

        //Act
        TreeSet<LocalDate> timeline =  client.getIndexTimeline(index);

        //Assert
        assertNotNull(timeline);
        assertFalse(timeline.isEmpty());
        assertTrue(timeline.size() > 5);
    }


    @Test
    @Order(3)
    @DisplayName("List timeline for Nasdaq 100")
    public void timelineNasdaq100() {
        //Arrange
        String index = "^IXIC";

        //Act
        TreeSet<LocalDate> timeline =  client.getIndexTimeline(index);

        //Assert
        assertNotNull(timeline);
        assertFalse(timeline.isEmpty());
        assertTrue(timeline.size() > 5);
    }

    @Test
    @Order(4)
    @DisplayName("List timeline for Dow Jones")
    public void timelineDowJones() {
        //Arrange
        String index = "^DJI";

        //Act
        TreeSet<LocalDate> timeline =  client.getIndexTimeline(index);

        //Assert
        assertNotNull(timeline);
        assertFalse(timeline.isEmpty());
        assertTrue(timeline.size() > 5);
    }


    @Test
    @Order(5)
    @DisplayName("Get last composition for SP500")
    public void compositionSP500() {
        //Arrange
        String index = "^GSPC";

        //Act
        Map<String, Float> composition =  client.getLastIndexComposition(index);

        //Assert
        assertNotNull(composition);
        assertFalse(composition.isEmpty());
        assertTrue(composition.size() > 5);
    }




}
