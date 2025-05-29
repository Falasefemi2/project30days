package com.femiproject.githubuser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class GithubActivity {

    private final ObjectMapper objectMapper;

    public GithubActivity() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void fetchAndDisplayActivity(String username) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/users/" + username + "/events"))
                    .header("User-Agent", "Github-Activity-CLI")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new IOException("User '" + username + "' not found. GitHub API returned 404.");
            } else if (response.statusCode() != 200) {
                throw new RuntimeException("Failed to fetch data. HTTP status: " + response.statusCode());
            }

            saveToJsonFile(response.body(), username);

            displayFormattedActivity(response.body());
        }
    }

    private void saveToJsonFile(String jsonData, String username) {
        try {
            Object json = objectMapper.readValue(jsonData, Object.class);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(username + "_github_activity.json"), json);
            System.out.println("✓ Activity saved to " + username + "_github_activity.json\n");
        } catch (IOException e) {
            System.err.println("Warning: Could not save to file - " + e.getMessage());
        }
    }

    private void displayFormattedActivity(String jsonData) throws IOException {
        List<Map<String, Object>> events = objectMapper.readValue(jsonData, new TypeReference<List<Map<String, Object>>>() {
        });

        if (events.isEmpty()) {
            System.out.println("No recent activity found for this user.");
            return;
        }

        System.out.println("Recent Actiivty:");
        System.out.println("====================");

        for (Map<String, Object> event : events) {
            String eventType = (String) event.get("type");
            Map<String, Object> repo = (Map<String, Object>) event.get("repo");
            String repoName = repo != null ? (String) repo.get("name") : "unknown";

            String formattedEvent = formatEvent(eventType, event, repoName);
            if (formattedEvent != null) {
                System.out.println("- " + formattedEvent);
            }
        }
    }

    private String formatEvent(String eventType, Map<String, Object> event, String repoName) {
        switch (eventType) {
            case "PushEvent" -> {
                Map<String, Object> payload = (Map<String, Object>) event.get("payload");
                if (payload != null) {
                    Object commitsObj = payload.get("commits");
                    int commitCount = 0;
                    if (commitsObj instanceof List) {
                        commitCount = ((List<?>) commitsObj).size();
                    }
                    return String.format("Pushed %d commit%s to %s",
                            commitCount, commitCount == 1 ? "" : "s", repoName);
                }
                return "Pushed commits to " + repoName;
            }

            case "IssuesEvent" -> {
                Map<String, Object> issuePayload = (Map<String, Object>) event.get("payload");
                if (issuePayload != null) {
                    String action = (String) issuePayload.get("action");
                    if ("opened".equals(action)) {
                        return "Opened a new issue in " + repoName;
                    } else if ("closed".equals(action)) {
                        return "Closed an issue in " + repoName;
                    }
                    return action + " an issue in " + repoName;
                }
                return "Updated an issue in " + repoName;
            }

            case "WatchEvent" -> {
                return "Starred " + repoName;
            }

            case "CreateEvent" -> {
                Map<String, Object> createPayload = (Map<String, Object>) event.get("payload");
                if (createPayload != null) {
                    String refType = (String) createPayload.get("ref_type");
                    if ("repository".equals(refType)) {
                        return "Created repository " + repoName;
                    } else if ("branch".equals(refType)) {
                        String ref = (String) createPayload.get("ref");
                        return "Created branch " + ref + " in " + repoName;
                    }
                }
                return "Created something in " + repoName;
            }

            case "DeleteEvent" -> {
                Map<String, Object> deletePayload = (Map<String, Object>) event.get("payload");
                if (deletePayload != null) {
                    String refType = (String) deletePayload.get("ref_type");
                    String ref = (String) deletePayload.get("ref");
                    return "Deleted " + refType + " " + ref + " in " + repoName;
                }
                return "Deleted something in " + repoName;
            }

            case "ForkEvent" -> {
                return "Forked " + repoName;
            }

            case "PullRequestEvent" -> {
                Map<String, Object> prPayload = (Map<String, Object>) event.get("payload");
                if (prPayload != null) {
                    String action = (String) prPayload.get("action");
                    if ("opened".equals(action)) {
                        return "Opened a pull request in " + repoName;
                    } else if ("closed".equals(action)) {
                        return "Closed a pull request in " + repoName;
                    } else if ("merged".equals(action)) {
                        return "Merged a pull request in " + repoName;
                    }
                    return action + " a pull request in " + repoName;
                }
                return "Updated a pull request in " + repoName;
            }

            case "ReleaseEvent" -> {
                return "Published a release in " + repoName;
            }

            case "PublicEvent" -> {
                return "Made " + repoName + " public";
            }

            default -> {
                return eventType.replace("Event", "") + " in " + repoName;
            }
        }
    }
}
