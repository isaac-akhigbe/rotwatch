package com.isaacakhigbe.rotwatch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;

class LinkCheckerTest {
    @Test
    void returns404ForMissingPage() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/missing", exchange -> {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
        });
        server.start();
        int port = server.getAddress().getPort();
        String url = "http://localhost:" + port + "/missing";
        URI uri = URI.create(url);
        LinkChecker linkChecker = new LinkChecker();
        int statusCode = linkChecker.check(uri);
        server.stop(0);

        assertEquals(404, statusCode);
    }
}
