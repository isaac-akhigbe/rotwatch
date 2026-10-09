package com.isaacakhigbe.rotwatch;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkExtractor {
    public List<String> extract(String html, String baseUrl) {
        Document document = Jsoup.parse(html, baseUrl);
        List<String> urls = new ArrayList<>();
        Elements elementLinks = document.select("a[href]");
        for (Element elementLink : elementLinks) {
            String link = elementLink.absUrl("href");
            if (link.startsWith("http://") || link.startsWith("https://")) {
                urls.add(link);
            }
        }
        return urls;
    }
}
