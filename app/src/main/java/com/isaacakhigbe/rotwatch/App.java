package com.isaacakhigbe.rotwatch;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage rotwatch <url>");
            System.exit(1);
        }

        String url = args[0];
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        int statusCode = response.statusCode();

        System.out.println(statusCode + " " + url);
    }
}
