package com.noetherlab.client;

import com.noetherlab.client.model.GeomBrownian;
import com.noetherlab.client.model.Security;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Models API Test")
public class ModelsTest extends ClientTest {

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("Get models by Security Ids")
    public void listByIds() {
        //Arrange
        SecuritiesQuery query = new SecuritiesQuery();
        query.getSecurityIds().add("XNAS:AMZN");
        query.getSecurityIds().add("XNYS:UNH");
        query.getSecurityIds().add("XNAS:AAPL");

        //Act
        Map<String, GeomBrownian> models = client.getModels(query);

        //Assert
        assertNotNull(models);
        assertEquals(query.getSecurityIds().size(), models.size());
    }




}
