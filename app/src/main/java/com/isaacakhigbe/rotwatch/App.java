package com.isaacakhigbe.rotwatch;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.time.Instant;
import java.time.Duration;

public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage rotwatch <url>");
            System.exit(1);
        }

        String url = args[0];
        URI uri = URI.create(url);

        Crawler crawler = new Crawler();
        Instant startTime = Instant.now();
        Set<String> links;
        try {
            links = crawler.crawl(uri);
        } catch (IOException e) {
            System.err.println("rotwatch: cannot fetch " + url + " (" + e + ")");
            System.exit(1);
            return;
        }

        Instant crawlDone = Instant.now();
        List<String> linkList = List.copyOf(links);

        LinkChecker linkChecker = new LinkChecker();
        List<Future<CheckResult>> futures = new ArrayList<>();

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String link : linkList) {
                URI linkUri = URI.create(link);
                Future<CheckResult> future = executor.submit(() -> linkChecker.check(linkUri));
                futures.add(future);
            }

            for (int i = 0; i < futures.size(); i++) {
                String link = linkList.get(i);
                CheckResult result = futures.get(i).get();
                switch (result) {
                    case Response(int statusCode) -> System.out.println(statusCode + " " + link);
                    case Failure(String errMessage) -> System.out.println("ERROR " + errMessage + " " + link);
                }
            }
        }

        Instant endTime = Instant.now();
        System.err.println("crawled in " + Duration.between(startTime, crawlDone).toMillis() + "ms");
        System.err.println(
                "checked " + links.size() + " links in " + Duration.between(crawlDone, endTime).toMillis() + "ms");
    }
}
