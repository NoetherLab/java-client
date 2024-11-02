package com.noetherlab.client;


import com.noetherlab.client.model.SECSecurity;
import com.noetherlab.client.model.Security;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Statement API Test")
public class StatementTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("Retrieve statement for AMZN")
    public void getStatementAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 10);
    }



    @Test
    @Order(2)
    @DisplayName("Retrieve standardized statement for AMZN")
    public void getStdStatementAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStdStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 10);
    }



    @Test
    @Order(3)
    @DisplayName("Retrieve statement for HPE")
    public void getStatementHPE() {
        //Arrange
        Security security = Security.fromId("XNYS:HPE");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 9);
    }



    @Test
    @Order(4)
    @DisplayName("Retrieve standardized statement for HPE")
    public void getStdStatementHPE() {
        //Arrange
        Security security = Security.fromId("XNYS:HPE");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStdStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 9);
    }


    @Test
    @Order(5)
    @DisplayName("Retrieve statement for EPAM")
    public void getStatementEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 10);
    }



    @Test
    @Order(6)
    @DisplayName("Retrieve standardized statement for EPAM")
    public void getStdStatementEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        SortedDataFrame<LocalDate, String, Float> df = client.getStdStatement(security);

        //Assert
        assertNotNull(df);
        assertTrue(df.getData().columnKeySet().size() > 20);
        assertTrue(df.getData().rowKeySet().size() > 4 * 10);
    }


    @Test
    @Order(7)
    @DisplayName("Retrieve standardized SEC securities for EPAM")
    public void getEdgarSecuritiesEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        Collection<SECSecurity> securities = client.getEdgarSecurities(security);

        //Assert
        assertNotNull(securities);
        assertFalse(securities.isEmpty());
    }




}
