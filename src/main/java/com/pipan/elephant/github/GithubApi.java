package com.pipan.elephant.github;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;

public class GithubApi {

    public GithubApi() {
        
    }

    public JSONArray releases(String repository) throws Exception {    
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/" + repository + "/releases"))
            .setHeader("Accept", "application/vnd.github.v3+json")
            .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new StatusNotOkException("Github responded with status code " + response.statusCode());
        }

        try {
            return new JSONArray(response.body());
        } catch (JSONException ex) {
            throw new ResponseParseException("Cannot parse github response: " + ex.getMessage());
        }
    }
}
