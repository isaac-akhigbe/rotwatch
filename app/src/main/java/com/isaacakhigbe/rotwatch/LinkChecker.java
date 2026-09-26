package com.isaacakhigbe.rotwatch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LinkChecker {
    private final HttpClient client = HttpClient.newHttpClient();

    public int check(URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        return response.statusCode();
    }
}
