package com.isaacakhigbe.rotwatch;

import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CrawlerTest {

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
}
