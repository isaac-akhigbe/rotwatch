package com.isaacakhigbe.rotwatch;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

public class LinkChecker {
    // Report redirects instead of following them
    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    // One semaphore per host, so no site gets more than a few requests at once
    private final ConcurrentHashMap<String, Semaphore> hostLimits = new ConcurrentHashMap<>();

    public CheckResult check(URI uri) throws Exception {
        try {
            Semaphore permits = hostLimits.computeIfAbsent(
                    uri.getHost(),
                    host -> new Semaphore(HttpDefaults.MAX_REQUESTS_PER_HOST));
            permits.acquire();
            try {
                HttpRequest request = HttpRequest.newBuilder(uri)
                        .header("User-Agent", HttpDefaults.USER_AGENT)
                        .timeout(Duration.ofSeconds(15))
                        .build();
                HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
                return new Response(response.statusCode());
            } finally {
                permits.release();
            }
        } catch (IOException e) {
            return new Failure(e.toString());
        }
    }
}