package com.isaacakhigbe.rotwatch;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class CrawlerTest {
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

    private void servePage(String path, String html) {
        server.createContext(path, exchange -> {
            byte[] body = html.getBytes();
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
    }

    private String baseUrl() {
        return "http://localhost:" + server.getAddress().getPort();
    }


    @Test
    void sameSiteReturnsTrue() {
        String startUrl = "https://isaacakhigbe.xyz/";
        String linkUrl = "https://isaacakhigbe.xyz/about";
        URI mainUri = URI.create(startUrl);
        URI linkUri = URI.create(linkUrl);
        Crawler crawler = new Crawler();
        assertTrue(crawler.isSameSite(mainUri, linkUri));
    }

    @Test
    void differentSiteReturnsFalse() {
        String startUrl = "https://isaacakhigbe.xyz/";
        String linkUrl = "https://github.com/isaac-akhigbe";
        URI mainUri = URI.create(startUrl);
        URI linkUri = URI.create(linkUrl);
        Crawler crawler = new Crawler();
        assertFalse(crawler.isSameSite(mainUri, linkUri));
    }

    @Test
    void sameSiteIgnoreCase() {
        String startUrl = "https://isaacakhigbe.xyz/";
        String linkUrl = "https://IsaacAkhigbe.xyz/about";
        URI mainUri = URI.create(startUrl);
        URI linkUri = URI.create(linkUrl);
        Crawler crawler = new Crawler();
        assertTrue(crawler.isSameSite(mainUri, linkUri));
    }


    @Test
    void crawlsEachPageOnceAndCollectsEveryLink() {
        servePage("/about", """
                <a href="https://github.com/isaac">GitHub</a>
                """);
        servePage("/blog", """
                <a href="/">Home</a>
                """);
        servePage("/", """
                <a href="/about">About</a>
                <a href="/blog">Blog</a>
                """);

        Crawler crawler = new Crawler();
        URI start = URI.create(baseUrl() + "/");

        Set<String> found = assertTimeoutPreemptively(
                Duration.ofSeconds(5),
                () -> crawler.crawl(start));

        List<String> expected = List.of(
                baseUrl() + "/about",
                baseUrl() + "/blog",
                "https://github.com/isaac",
                baseUrl() + "/");
        assertEquals(expected, List.copyOf(found));
    }
}
