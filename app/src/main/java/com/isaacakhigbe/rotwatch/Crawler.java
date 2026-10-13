package com.isaacakhigbe.rotwatch;

import java.net.URI;
import java.util.Set;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.io.IOException;

public class Crawler {

    private final PageFetcher pageFetcher = new PageFetcher();
    private final LinkExtractor linkExtractor = new LinkExtractor();

    boolean isSameSite(URI start, URI link) {
        String startHost = start.getHost();
        String linkHost = link.getHost();
        return startHost.equalsIgnoreCase(linkHost);
    }

    public Set<String> crawl(URI start) throws IOException, InterruptedException {
        Queue<URI> toVisit = new ArrayDeque<>();
        toVisit.add(start);
        Set<URI> visited = new HashSet<>();
        Set<String> found = new LinkedHashSet<>();

        while (!toVisit.isEmpty()) {
            URI uri = toVisit.poll();
            if (visited.add(uri)) {
                String page = pageFetcher.fetch(uri);
                List<String> links = linkExtractor.extract(page, uri.toString());

                for (String link : links) {
                    URI linkUri = URI.create(link);
                    if (isSameSite(start, linkUri)) {
                        toVisit.add(linkUri);
                    }
                    found.add(link);
                }

            }
        }

        return found;
    }
}
