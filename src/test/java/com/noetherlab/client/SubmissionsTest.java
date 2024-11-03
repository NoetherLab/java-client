package com.noetherlab.client;

import com.noetherlab.client.model.Announcement;
import com.noetherlab.client.model.Security;
import com.noetherlab.client.model.Submission;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("List SEC announcements for EPAM")
    public void getAnnouncementsEPAM() {
        //Arrange
        Security security = Security.fromId("XNYS:EPAM");

        //Act
        Collection<Announcement> announcements = client.getAnnouncements(security.getId());

        //Assert
        assertNotNull(announcements);
        assertTrue(announcements.size() > 100);
    }


    @Disabled
    @Test
    @Order(5)
    @DisplayName("Head SEC submission for CHK data")
    public void headSubmission() {
        //Arrange
        Security security = Security.fromId("XNAS:CHK");
        String accessionNumber = "0000895126-08-000351";

        //Act
        Map<String, Object> head = client.headSubmission(security.getId(), accessionNumber);

        //Assert
        assertNotNull(head);
        assertEquals(head.size(), 2);
        assertTrue(head.containsKey("Content-Length"));
        assertTrue(head.containsKey("Last-modified"));
    }



    @Test
    @Order(6)
    @DisplayName("Get SEC submission for CHK data")
    public void getSubmissionCHK_exists() {
        //Arrange
        Security security = Security.fromId("XNAS:CHK");
        String accessionNumber = "0000895126-08-000351";

        //Act
        byte[] submissions = client.getSubmission(security.getId(), accessionNumber);

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.length > 100);
    }

    @Test
    @Order(7)
    @DisplayName("Get SEC submission for CHK - not existing")
    public void getSubmissionCHK_not_exists() {
        //Arrange
        Security security = Security.fromId("XNAS:CHK");
        String accessionNumber = "0000895126-15-000177";

        //Act
        byte[] submissions = client.getSubmission(security.getId(), accessionNumber);

        //Assert
        assertNull(submissions);
    }

    @Disabled
    @Test
    @Order(8)
    @DisplayName("Head SEC submission content for CHK")
    public void headSubmissionContent() {
        //Arrange
        Security security = Security.fromId("XNAS:CHK");
        String accessionNumber = "0000895126-08-000351";

        //Act
        Map<String, Object> head = client.headSubmissionContent(security.getId(), accessionNumber);

        //Assert
        assertNotNull(head);
        assertEquals(head.size(), 2);
        assertTrue(head.containsKey("Content-Length"));
        assertTrue(head.containsKey("Last-modified"));
    }


    @Test
    @Order(9)
    @DisplayName("Get SEC submission content for CHK")
    public void getSubmissionContentAMZN() {
        //Arrange
        Security security = Security.fromId("XNAS:CHK");
        String accessionNumber = "0000895126-08-000351";

        //Act
        Map<String, String> submissions = client.getSubmissionContent(security.getId(), accessionNumber);

        //Assert
        assertNotNull(submissions);
        assertTrue(submissions.size() > 1);
    }



    @Test
    @Order(10)
    @DisplayName("Search announcements by content")
    public void searchAnnouncements() {
        //Arrange
        String query = "earnings";
        int limit = 10;

        //Act
        List<Map<String, Object>> announcements = client.searchAnnouncements(query, limit);

        //Assert
        assertNotNull(announcements);
        assertEquals(limit, announcements.size());
    }


}
