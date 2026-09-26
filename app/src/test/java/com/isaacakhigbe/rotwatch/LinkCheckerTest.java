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

    private static int getStatusCode(String url) throws Exception {
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
        assertEquals(404, getStatusCode(serve("/missing", 404)));
    }

    @Test
    void returns200ForExistingPage() throws Exception {
        assertEquals(200, getStatusCode(serve("/about", 200)));
    }
}

    