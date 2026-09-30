package com.isaacakhigbe.rotwatch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;

class LinkCheckerTest {

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

    private static CheckResult checkUrl(String url) throws Exception {
        URI uri = URI.create(url);
        LinkChecker linkChecker = new LinkChecker();
        return linkChecker.check(uri);
    }

    private String serve(String path, int status) {
        server.createContext(path, exchange -> {
            exchange.sendResponseHeaders(status, -1);
            exchange.close();
        });
        return "http://localhost:" + server.getAddress().getPort() + path;
    }

    @Test
    void returns404ForMissingPage() throws Exception {
        assertEquals(new Response(404), checkUrl(serve("/missing", 404)));
    }

    @Test
    void returns200ForExistingPage() throws Exception {
        assertEquals(new Response(200), checkUrl(serve("/about", 200)));
    }

    @Test
    void returnsFailureWhenNothingIsListening() throws Exception {
        String url = serve("/about", 200);
        server.stop(0);
        CheckResult result = checkUrl(url);
        assertInstanceOf(Failure.class, result);
    }
}
