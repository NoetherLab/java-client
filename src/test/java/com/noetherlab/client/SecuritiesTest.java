package com.noetherlab.client;


import com.noetherlab.client.model.Security;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Security API Test")
public class SecuritiesTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("Filter securities by Security Ids")
    public void listByIds() {
        //Arrange
        SecuritiesQuery query = new SecuritiesQuery();
        query.getSecurityIds().add("XNAS:AMZN");
        query.getSecurityIds().add("XNYS:UNH");
        query.getSecurityIds().add("XNAS:AAPL");

        //Act
        Collection<Security> securities = client.getSecurities(query);

        //Assert
        assertNotNull(securities);
        assertFalse(securities.isEmpty());
        assertEquals(query.getSecurityIds().size(), securities.size());
    }


    @Test
    @Order(2)
    @DisplayName("Filter securities by tags")
    public void listByTags() {
        //Arrange
        SecuritiesQuery query = new SecuritiesQuery();
        query.getTags().add("Strat.Cocoa");

        //Act
        Collection<Security> securities = client.getSecurities(query);

        //Assert
        assertNotNull(securities);
        assertFalse(securities.isEmpty());
        assertTrue(securities.size() > 3);
    }

    @Test
    @Order(3)
    @DisplayName("Filter securities by tags, portfolio")
    public void listByPortfolio() {
        //Arrange
        SecuritiesQuery query = new SecuritiesQuery();
        query.getTags().add("Portfolio");

        //Act
        Collection<Security> securities = client.getSecurities(query);

        //Assert
        assertNotNull(securities);
        assertFalse(securities.isEmpty());
        assertTrue(securities.size() > 3);
    }

    @Test
    @Order(4)
    @DisplayName("Filter securities by index (Dow Jones)")
    public void listByIndex() {
        //Arrange
        SecuritiesQuery query = new SecuritiesQuery();
        query.getIndices().add("^DJI");

        //Act
        Collection<Security> securities = client.getSecurities(query);

        //Assert
        assertNotNull(securities);
        assertFalse(securities.isEmpty());
        assertEquals(securities.size() , 30);
    }


}
