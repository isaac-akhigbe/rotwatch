package com.isaacakhigbe.rotwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashSet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkExtractor {
    public List<String> extract(String html, String baseUrl) {
        Document document = Jsoup.parse(html, baseUrl);
        LinkedHashSet<String> set = new LinkedHashSet<>();
        Elements elementLinks = document.select("a[href]");
        for (Element elementLink : elementLinks) {
            String link = elementLink.absUrl("href");
            int hashIndex = link.indexOf("#");
            if (hashIndex != -1) {
                link = link.substring(0, hashIndex);
            }
            if (link.startsWith("http://") || link.startsWith("https://")) {
                set.add(link);
            }
        }
        return new ArrayList<>(set);
    }
}
