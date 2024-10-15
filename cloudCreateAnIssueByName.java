import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JiraIssueCreator {

    // Define the Jira URL and credentials
    private static final String JIRA_URL = "https://yourcompany.atlassian.net"; // Replace with your company's Jira URL
    private static final String EMAIL = "your_email@example.com"; // Replace with your email
    private static final String API_TOKEN = "your_api_token"; // Replace with your API token

    public static void main(String[] args) {
        createIssue();
    }

    public static void createIssue() {
        try {
            // Construct the API endpoint URL for creating an issue
            String urlString = JIRA_URL + "/rest/api/3/issue";
            URL url = new URL(urlString);

            // Set up the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            // Prepare the headers, including authorization
            String auth = EMAIL + ":" + API_TOKEN;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // Define the issue details in JSON format
            String issueData = "{"
                    + "\"fields\": {"
                    + "\"project\": {"
                    + "\"key\": \"AFJ\"" // Replace with your project key
                    + "},"
                    + "\"summary\": \"New issue created via Java\","
                    + "\"description\": \"Description of the issue\","
                    + "\"issuetype\": {"
                    + "\"name\": \"Task\"" // Replace with the desired issue type name
                    + "}"
                    + "}"
                    + "}";

            // Enable the connection to send data
            connection.setDoOutput(true);

            // Write the payload to the output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = issueData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check the response code
            int responseCode = connection.getResponseCode();
            if (responseCode == 201) {
                // If successful, read the response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Issue created successfully!");
                    System.out.println("Response: " + response.toString());
                }
            } else {
                // If not successful, print the status code and response message
                System.out.println("Failed to create issue.");
                System.out.println("Status Code: " + responseCode);
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    System.out.println("Response: " + errorResponse.toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
