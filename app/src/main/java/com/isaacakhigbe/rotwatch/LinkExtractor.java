package com.isaacakhigbe.rotwatch;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkExtractor {
    public List<String> extract(String html) {
        Document document = Jsoup.parse(html);
        List<String> urls = new ArrayList<>();
        Elements links = document.select("a[href]");
        for (Element link : links) {
            urls.add(link.attr("href"));
        }
        return urls;
    }
}
