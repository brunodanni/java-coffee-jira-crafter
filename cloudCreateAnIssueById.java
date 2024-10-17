// This Java application demonstrates how to create an issue in Jira using its REST API.
// Developed using Apache NetBeans with Maven for dependency management.

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JiraIssueCreator {

    public static void main(String[] args) {
        try {
            // Define the Jira API endpoint for creating issues
            String urlString = "https://yourcompany.atlassian.net/rest/api/3/issue"; // Replace with your Jira URL
            URL url = new URL(urlString);

            // Set up the HTTP connection for a POST request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

            // Set up authentication using email and API token
            // Replace with your email and API token
            String userCredentials = "your_email@example.com:your_api_token";
            String basicAuth = "Basic " + Base64.getEncoder().encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", basicAuth);

            // Define the JSON payload with issue details
            String payload = "{"
                    + "\"fields\": {"
                    + "\"project\": {"
                    + "\"id\": \"10028\"" // Replace with actual project ID
                    + "},"
                    + "\"summary\": \"New issue created via Java\"," // Brief title of the issue
                    + "\"description\": {"
                    + "\"content\": ["
                    + "{"
                    + "\"content\": ["
                    + "{"
                    + "\"text\": \"Order entry fails when selecting supplier.\","
                    + "\"type\": \"text\""
                    + "}"
                    + "],"
                    + "\"type\": \"paragraph\""
                    + "}"
                    + "],"
                    + "\"type\": \"doc\","
                    + "\"version\": 1"
                    + "},"
                    + "\"issuetype\": {"
                    + "\"id\": \"10006\"" // Replace with appropriate issue type ID
                    + "}"
                    + "}"
                    + "}";

            // Enable output stream for sending data
            connection.setDoOutput(true);

            // Write the payload to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Get the response code to determine success or failure
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read and print the response from the input stream
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println("Response: " + response.toString());
            }

        } catch (Exception e) {
            // Print the stack trace if an exception occurs
            e.printStackTrace();
        }
    }
}
