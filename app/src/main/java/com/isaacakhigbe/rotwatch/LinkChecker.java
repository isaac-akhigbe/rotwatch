package com.isaacakhigbe.rotwatch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class LinkChecker {
    // Deliberately make the client to not follow redirection
    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();

    public CheckResult check(URI uri) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder(uri).build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return new Response(response.statusCode());
        } catch (IOException e) {
            return new Failure(e.toString());
        }
    }
}
