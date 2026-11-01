package com.isaacakhigbe.rotwatch;

import java.net.URI;
import java.util.Set;
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
        Set<String> links = crawler.crawl(uri);

        LinkChecker linkChecker = new LinkChecker();
        for (String link : links) {
            URI linkUri = URI.create(link);

            CheckResult result = linkChecker.check(linkUri);

            switch (result) {
                case Response(int statusCode) -> System.out.println(statusCode + " " + link);
                case Failure(String errMessage) -> System.out.println("ERROR " + errMessage + " " + link);
            }
        }
        Instant endTime = Instant.now();
        long diff = Duration.between(startTime, endTime).toMillis();
        System.err.println("checked " + links.size() + " links in " + diff + "ms");
    }
}
