package com.isaacakhigbe.rotwatch;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class LinkExtractorTest {
    @Test
    void extractsLinksFromMessyHtml() {
        LinkExtractor linkExtractor = new LinkExtractor();
        String html = """
                <a href="/about">About</a>
                <a href='https://github.com/isaac'>GitHub</a>
                <A HREF="/blog" class="nav">Blog</A>
                <!-- <a href="/secret">old link</a> -->
                """;
        List<String> links = linkExtractor.extract(html);
        List<String> expected = List.of("/about", "https://github.com/isaac", "/blog");
        assertEquals(expected, links);
    }
}
