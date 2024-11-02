package com.noetherlab.client;

import com.noetherlab.client.model.Security;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Prices API Test")
public class PriceTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("Retrieve price for AMZN")
    public void getPriceAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getPrice(security);

        //Assert
        assertNotNull(price);
        assertTrue(price.getData().columnKeySet().size() > 20);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }

    @Test
    @Order(2)
    @DisplayName("Retrieve price for HPE")
    public void getPriceHPE() {
        //Arrange
        Security security = Security.fromId("XNYS:HPE");

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getPrice(security);

        //Assert
        assertNotNull(price);
        assertTrue(price.getData().columnKeySet().size() > 20);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }


    @Test
    @Order(3)
    @DisplayName("Retrieve price for EPAM")
    public void getPriceEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getPrice(security);

        //Assert
        assertNotNull(price);
        assertTrue(price.getData().columnKeySet().size() > 4);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }


    @Test
    @Order(4)
    @DisplayName("Retrieve currency for EUR")
    public void getCurrencyEUR() {
        //Arrange
        String currency = "EUR";

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getCurrency(currency);

        //Assert
        assertNotNull(price);
        assertEquals(price.getData().columnKeySet().size(), 6);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }


    @Test
    @Order(5)
    @DisplayName("Retrieve currency for INR")
    public void getCurrencyINR() {
        //Arrange
        String currency = "INR";

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getCurrency(currency);

        //Assert
        assertNotNull(price);
        assertEquals(price.getData().columnKeySet().size(), 6);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }


    @Test
    @Order(6)
    @DisplayName("Retrieve currency for JPY")
    public void getCurrencyJPY() {
        //Arrange
        String currency = "JPY";

        //Act
        SortedDataFrame<LocalDate, String, Float> price = client.getCurrency(currency);

        //Assert
        assertNotNull(price);
        assertEquals(price.getData().columnKeySet().size(), 6);
        assertTrue(price.getData().rowKeySet().size() > 365);
    }

    @Test
    @Order(7)
    @DisplayName("Retrieve environment")
    public void getEnvironment() {
        //Arrange

        //Act
        SortedDataFrame<LocalDate, String, Float> environment = client.getEnvironment();

        //Assert
        assertNotNull(environment);
        assertEquals(environment.getData().columnKeySet().size(), 3);
        assertTrue(environment.getData().rowKeySet().size() > 12 * 10);
    }



}
