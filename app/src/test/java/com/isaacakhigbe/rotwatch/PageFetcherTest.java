package com.isaacakhigbe.rotwatch;

import java.net.InetSocketAddress;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import java.net.URI;

class PageFetcherTest {
    private HttpServer server;

    @BeforeEach
    void startServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private static String fetchUrl(String url) throws Exception {
        URI uri = URI.create(url);
        PageFetcher pageFetcher = new PageFetcher();
        return pageFetcher.fetch(uri);
    }

    @Test
    void returnsHtmlBodyOfPage() throws Exception {
        String path = "/about";
        String expectedBody = "<a href=\"/about\">About</a>";
        server.createContext(path, exchange -> {
            byte[] body = expectedBody.getBytes();
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        String url = "http://localhost:" + server.getAddress().getPort() + path;
        assertEquals(expectedBody, fetchUrl(url));
    }

}
