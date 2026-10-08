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
                <a href="contact">Contact</a>
                <a href="../index.html">Home</a>
                """;
        String baseUrl = "https://isaacakhigbe.xyz/blog/post";
        List<String> links = linkExtractor.extract(html, baseUrl);
        List<String> expected = List.of("https://isaacakhigbe.xyz/about", "https://github.com/isaac",
                "https://isaacakhigbe.xyz/blog", "https://isaacakhigbe.xyz/blog/contact",
                "https://isaacakhigbe.xyz/index.html");
        assertEquals(expected, links);
    }
}
