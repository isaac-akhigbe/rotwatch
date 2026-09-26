package com.isaacakhigbe.rotwatch;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

class LinkCheckerTest {
    @Test
    void serverStartsOnAFreePort() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.start();
        int port = server.getAddress().getPort();
        server.stop(0);
        assertTrue(port > 0);
    }
}
