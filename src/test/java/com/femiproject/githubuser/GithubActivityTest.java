package com.femiproject.githubuser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GithubActivityTest {

    private GithubActivity githubActivity;
    private ByteArrayOutputStream outputStream;
    private ByteArrayOutputStream errorStream;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        githubActivity = new GithubActivity();

        outputStream = new ByteArrayOutputStream();
        errorStream = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(errorStream));
    }

    @Test
    void testFormatEvent_PushEvent_SingleCommit() {
        // Given
        Map<String, Object> event = createPushEvent(1);

        // When
        String result = invokeFormatEvent("PushEvent", event, "user/repo");

        // Then
        assertEquals("Pushed 1 commit to user/repo", result);
    }

    @Test
    void testFormatEvent_PushEvent_MultipleCommits() {
        // Given
        Map<String, Object> event = createPushEvent(5);

        // When
        String result = invokeFormatEvent("PushEvent", event, "user/repo");

        // Then
        assertEquals("Pushed 5 commits to user/repo", result);
    }

    @Test
    void testFormatEvent_PushEvent_NoPayload() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("PushEvent", event, "user/repo");

        // Then
        assertEquals("Pushed commits to user/repo", result);
    }

    @Test
    void testFormatEvent_IssuesEvent_Opened() {
        // Given
        Map<String, Object> event = createIssuesEvent("opened");

        // When
        String result = invokeFormatEvent("IssuesEvent", event, "user/repo");

        // Then
        assertEquals("Opened a new issue in user/repo", result);
    }

    @Test
    void testFormatEvent_IssuesEvent_Closed() {
        // Given
        Map<String, Object> event = createIssuesEvent("closed");

        // When
        String result = invokeFormatEvent("IssuesEvent", event, "user/repo");

        // Then
        assertEquals("Closed an issue in user/repo", result);
    }

    @Test
    void testFormatEvent_IssuesEvent_CustomAction() {
        // Given
        Map<String, Object> event = createIssuesEvent("reopened");

        // When
        String result = invokeFormatEvent("IssuesEvent", event, "user/repo");

        // Then
        assertEquals("reopened an issue in user/repo", result);
    }

    @Test
    void testFormatEvent_IssuesEvent_NoPayload() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("IssuesEvent", event, "user/repo");

        // Then
        assertEquals("Updated an issue in user/repo", result);
    }

    @Test
    void testFormatEvent_WatchEvent() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("WatchEvent", event, "user/repo");

        // Then
        assertEquals("Starred user/repo", result);
    }

    @Test
    void testFormatEvent_CreateEvent_Repository() {
        // Given
        Map<String, Object> event = createCreateEvent("repository", null);

        // When
        String result = invokeFormatEvent("CreateEvent", event, "user/repo");

        // Then
        assertEquals("Created repository user/repo", result);
    }

    @Test
    void testFormatEvent_CreateEvent_Branch() {
        // Given
        Map<String, Object> event = createCreateEvent("branch", "feature-branch");

        // When
        String result = invokeFormatEvent("CreateEvent", event, "user/repo");

        // Then
        assertEquals("Created branch feature-branch in user/repo", result);
    }

    @Test
    void testFormatEvent_CreateEvent_Unknown() {
        // Given
        Map<String, Object> event = createCreateEvent("tag", "v1.0.0");

        // When
        String result = invokeFormatEvent("CreateEvent", event, "user/repo");

        // Then
        assertEquals("Created something in user/repo", result);
    }

    @Test
    void testFormatEvent_DeleteEvent() {
        // Given
        Map<String, Object> event = createDeleteEvent("branch", "old-feature");

        // When
        String result = invokeFormatEvent("DeleteEvent", event, "user/repo");

        // Then
        assertEquals("Deleted branch old-feature in user/repo", result);
    }

    @Test
    void testFormatEvent_ForkEvent() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("ForkEvent", event, "user/repo");

        // Then
        assertEquals("Forked user/repo", result);
    }

    @Test
    void testFormatEvent_PullRequestEvent_Opened() {
        // Given
        Map<String, Object> event = createPullRequestEvent("opened");

        // When
        String result = invokeFormatEvent("PullRequestEvent", event, "user/repo");

        // Then
        assertEquals("Opened a pull request in user/repo", result);
    }

    @Test
    void testFormatEvent_PullRequestEvent_Closed() {
        // Given
        Map<String, Object> event = createPullRequestEvent("closed");

        // When
        String result = invokeFormatEvent("PullRequestEvent", event, "user/repo");

        // Then
        assertEquals("Closed a pull request in user/repo", result);
    }

    @Test
    void testFormatEvent_PullRequestEvent_Merged() {
        // Given
        Map<String, Object> event = createPullRequestEvent("merged");

        // When
        String result = invokeFormatEvent("PullRequestEvent", event, "user/repo");

        // Then
        assertEquals("Merged a pull request in user/repo", result);
    }

    @Test
    void testFormatEvent_ReleaseEvent() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("ReleaseEvent", event, "user/repo");

        // Then
        assertEquals("Published a release in user/repo", result);
    }

    @Test
    void testFormatEvent_PublicEvent() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("PublicEvent", event, "user/repo");

        // Then
        assertEquals("Made user/repo public", result);
    }

    @Test
    void testFormatEvent_UnknownEvent() {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent("CustomEvent", event, "user/repo");

        // Then
        assertEquals("Custom in user/repo", result);
    }

    @Test
    void testFetchAndDisplayActivity_Success() throws IOException, InterruptedException {
        // Given
        String username = "testuser";
        String jsonResponse = createValidJsonResponse();

        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient mockClient = mock(HttpClient.class);
            HttpResponse<String> mockResponse = mock(HttpResponse.class);

            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockClient);
            when(mockResponse.statusCode()).thenReturn(200);
            when(mockResponse.body()).thenReturn(jsonResponse);
            when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // When
            githubActivity.fetchAndDisplayActivity(username);

            // Then
            String output = outputStream.toString();
            assertTrue(output.contains("Activity saved to testuser_github_activity.json"));
            assertTrue(output.contains("Recent Actiivty:")); // Note: matches your typo in the code
            assertTrue(output.contains("Starred user/repo"));
        }
    }

    @Test
    void testFetchAndDisplayActivity_UserNotFound() throws IOException, InterruptedException {
        // Given
        String username = "nonexistentuser";

        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient mockClient = mock(HttpClient.class);
            HttpResponse<String> mockResponse = mock(HttpResponse.class);

            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockClient);
            when(mockResponse.statusCode()).thenReturn(404);
            when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // When & Then
            IOException exception = assertThrows(IOException.class, () -> {
                githubActivity.fetchAndDisplayActivity(username);
            });

            assertEquals("User 'nonexistentuser' not found", exception.getMessage());
        }
    }

    @Test
    void testFetchAndDisplayActivity_ServerError() throws IOException, InterruptedException {
        // Given
        String username = "testuser";

        try (MockedStatic<HttpClient> mockedHttpClient = mockStatic(HttpClient.class)) {
            HttpClient mockClient = mock(HttpClient.class);
            HttpResponse<String> mockResponse = mock(HttpResponse.class);

            mockedHttpClient.when(HttpClient::newHttpClient).thenReturn(mockClient);
            when(mockResponse.statusCode()).thenReturn(500);
            when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                    .thenReturn(mockResponse);

            // When & Then
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                githubActivity.fetchAndDisplayActivity(username);
            });

            assertTrue(exception.getMessage().contains("Failed to fetch data. HTTP status: 500"));
        }
    }

    @Test
    void testDisplayFormattedActivity_EmptyEvents() throws IOException {
        // Given
        String emptyJsonData = "[]";

        // When
        invokeDisplayFormattedActivity(emptyJsonData);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("No recent activity found for this user."));
    }

    @Test
    void testDisplayFormattedActivity_WithEvents() throws IOException {
        // Given
        String jsonData = createValidJsonResponse();

        // When
        invokeDisplayFormattedActivity(jsonData);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Recent Actiivty:")); // Note: matches your typo
        assertTrue(output.contains("Starred user/repo"));
    }

    @Test
    void testSaveToJsonFile_Success() throws IOException {
        // Given
        String jsonData = "{\"test\": \"data\"}";
        String username = "testuser";

        // Change to temp directory for testing
        System.setProperty("user.dir", tempDir.toString());

        // When
        invokeSaveToJsonFile(jsonData, username);

        // Then
        String output = outputStream.toString();
        assertTrue(output.contains("Activity saved to testuser_github_activity.json"));

        // Verify file was created
        File savedFile = tempDir.resolve("testuser_github_activity.json").toFile();
        assertTrue(savedFile.exists());
    }

    @Test
    void testSaveToJsonFile_InvalidJson() {
        // Given
        String invalidJson = "invalid json";
        String username = "testuser";

        // When
        invokeSaveToJsonFile(invalidJson, username);

        // Then
        String errorOutput = errorStream.toString();
        assertTrue(errorOutput.contains("Warning: Could not save to file"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"PushEvent", "IssuesEvent", "WatchEvent", "ForkEvent", "CreateEvent",
        "DeleteEvent", "PullRequestEvent", "ReleaseEvent", "PublicEvent"})
    void testFormatEvent_AllEventTypes(String eventType) {
        // Given
        Map<String, Object> event = new HashMap<>();

        // When
        String result = invokeFormatEvent(eventType, event, "user/repo");

        // Then
        assertNotNull(result);
        assertTrue(result.contains("user/repo"));
    }

    // Helper methods to invoke private methods using reflection
    private String invokeFormatEvent(String eventType, Map<String, Object> event, String repoName) {
        try {
            var method = GithubActivity.class.getDeclaredMethod("formatEvent", String.class, Map.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(githubActivity, eventType, event, repoName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke formatEvent", e);
        }
    }

    private void invokeDisplayFormattedActivity(String jsonData) {
        try {
            var method = GithubActivity.class.getDeclaredMethod("displayFormattedActivity", String.class);
            method.setAccessible(true);
            method.invoke(githubActivity, jsonData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke displayFormattedActivity", e);
        }
    }

    private void invokeSaveToJsonFile(String jsonData, String username) {
        try {
            var method = GithubActivity.class.getDeclaredMethod("saveToJsonFile", String.class, String.class);
            method.setAccessible(true);
            method.invoke(githubActivity, jsonData, username);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke saveToJsonFile", e);
        }
    }

    // Helper methods to create test data
    private Map<String, Object> createPushEvent(int commitCount) {
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        List<Object> commits = new java.util.ArrayList<>();
        for (int i = 0; i < commitCount; i++) {
            commits.add(new HashMap<>());
        }

        payload.put("commits", commits);
        event.put("payload", payload);
        return event;
    }

    private Map<String, Object> createIssuesEvent(String action) {
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);
        event.put("payload", payload);
        return event;
    }

    private Map<String, Object> createCreateEvent(String refType, String ref) {
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("ref_type", refType);
        if (ref != null) {
            payload.put("ref", ref);
        }
        event.put("payload", payload);
        return event;
    }

    private Map<String, Object> createDeleteEvent(String refType, String ref) {
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("ref_type", refType);
        payload.put("ref", ref);
        event.put("payload", payload);
        return event;
    }

    private Map<String, Object> createPullRequestEvent(String action) {
        Map<String, Object> event = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", action);
        event.put("payload", payload);
        return event;
    }

    private String createValidJsonResponse() {
        return "[{\"type\":\"WatchEvent\",\"repo\":{\"name\":\"user/repo\"}}]";
    }
}
