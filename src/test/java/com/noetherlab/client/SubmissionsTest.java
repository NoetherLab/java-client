package com.noetherlab.client;

import com.noetherlab.client.model.Security;
import com.noetherlab.client.model.Submission;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Submission API Test")
public class SubmissionsTest extends ClientTest{

    @BeforeAll
    public void init() {
        initClient();
    }

    @Test
    @Order(1)
    @DisplayName("List SEC submission for AMZN")
    public void getSubmissionsAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");

        //Act
        Collection<Submission> submissions = client.getSubmissions(security.getId());

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.size() > 100);
    }

    @Test
    @Order(2)
    @DisplayName("List SEC submission for HPE")
    public void getSubmissionsHPE() {
        //Arrange
        Security security = Security.fromId("XNYS:HPE");

        //Act
        Collection<Submission> submissions = client.getSubmissions(security.getId());

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.size() > 100);
    }



    @Test
    @Order(3)
    @DisplayName("List SEC submission for EPAM")
    public void getSubmissionsEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        Collection<Submission> submissions = client.getSubmissions(security.getId());

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.size() > 100);
    }


    @Test
    @Order(4)
    @DisplayName("Get SEC submission for AMZN data")
    public void getSubmissionAMZN_exists() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");
        String accessionNumber = "0001193125-07-225370";

        //Act
        byte[] submissions = client.getSubmission(security.getId(), accessionNumber);

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.length > 100);
    }

    @Test
    @Order(5)
    @DisplayName("Get SEC submission for AMZN - not existing")
    public void getSubmissionAMZN_not_exists() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");
        String accessionNumber = "0001950047-23-003306";

        //Act
        byte[] submissions = client.getSubmission(security.getId(), accessionNumber);

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.length > 100);
    }


    @Test
    @Order(6)
    @DisplayName("Get SEC submission content for AMZN")
    public void getSubmissionContentAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:AMZN");
        String accessionNumber = "0001950047-23-003306";

        //Act
        Map<String, String> submissions = client.getSubmissionContent(security.getId(), accessionNumber);

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.size() > 1);
    }


}
