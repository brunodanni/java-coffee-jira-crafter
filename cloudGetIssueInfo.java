// This Java application demonstrates how to interact with the Jira REST API using Apache HttpClient.
// Developed using Apache NetBeans with Maven for dependency management.

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JiraApiExample {
    public static void main(String[] args) throws Exception {
        // Jira API endpoint for retrieving issue details
        String jiraUrl = "https://yourdomain.atlassian.net/rest/api/2/issue/ISSUE-KEY";

        // Credentials for authentication (replace with your email and API token)
        String email = "your-email@example.com";
        String apiToken = "your-api-token";

        // Create a default HTTP client
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // Create an HTTP GET request to retrieve issue details
        HttpGet httpGet = new HttpGet(jiraUrl);

        // Encode credentials to Base64 for Basic Authentication
        String auth = email + ":" + apiToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        
        // Set HTTP headers for authentication and response format
        httpGet.setHeader("Authorization", "Basic " + encodedAuth);
        httpGet.setHeader("Accept", "application/json");

        // Execute the request and process the response
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            // Convert the response entity to a string
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            // Parse the JSON response using Jackson's ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Extract specific fields from the JSON response
            String issueKey = jsonNode.get("key").asText();
            String summary = jsonNode.get("fields").get("summary").asText();
            String description = jsonNode.get("fields").get("description").asText();
            String status = jsonNode.get("fields").get("status").get("name").asText();
            String assignee = jsonNode.get("fields").get("assignee").get("displayName").asText();

            // Print the extracted fields for better readability
            System.out.println("Issue Key: " + issueKey);
            System.out.println("Summary: " + summary);
            System.out.println("Description: " + description);
            System.out.println("Status: " + status);
            System.out.println("Assignee: " + assignee);
        }
    }
}
