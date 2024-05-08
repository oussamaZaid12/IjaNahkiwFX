package test;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PredictionService {
    private final HttpClient client;
    private final String fastApiUrl;

    public PredictionService(String fastApiUrl) {
        this.client = HttpClient.newHttpClient();
        this.fastApiUrl = fastApiUrl;
    }

    public Map<String, Object> predict(List<Integer> scores) {
        String jsonPayload = new Gson().toJson(Map.of("features", scores));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fastApiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return parseResponse(response.body());
        } catch (Exception e) {
            return Map.of("success", false, "error", "Network error: " + e.getMessage());
        }
    }

    public Map<String, Object> predict2(List<Integer> scores) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fastApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(buildJson(scores)))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return parseResponse(response.body());
            } else {
                System.err.println("Failed to get a valid response from the API: " + response.statusCode());
                return Collections.singletonMap("success", false);
            }
        } catch (Exception e) {
            System.err.println("Error during API request: " + e.getMessage());
            return Collections.singletonMap("success", false);
        }
    }

    public Map<String, Object> predict3(List<Integer> features) {
        Map<String, Object> result = new HashMap<>();
        try {
            String requestBody = buildJson(features);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fastApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                result = parseResponse(response.body());
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("error", "Received non-200 response: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", "Error during API request: " + e.getMessage());
        }
        return result;
    }

    private String buildJson1(List<Integer> features) {
        // Transform the list of scores into a JSON array string
        String featureArray = features.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"features\": " + featureArray + "}";
    }

    private String buildJson(List<Integer> scores) {
        Gson gson = new Gson();
        Map<String, List<Integer>> payload = new HashMap<>();
        payload.put("features", scores);
        return gson.toJson(payload);
    }

    private Map<String, Object> parseResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("prediction", jsonObject.optInt("prediction", -1));  // Default to -1 if not found
            return result;
        } catch (JSONException e) {
            System.err.println("Failed to parse JSON response: " + e.getMessage());
            return Collections.singletonMap("success", false);
        }
    }

}
