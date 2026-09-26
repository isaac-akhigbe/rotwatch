package com.isaacakhigbe.rotwatch;

import java.net.URI;

public class App {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("usage rotwatch <url>");
            System.exit(1);
        }

        String url = args[0];
        URI uri = URI.create(url);
        LinkChecker linkChecker = new LinkChecker();
        int statusCode = linkChecker.check(uri);
        System.out.println(statusCode + " " + url);
    }
}
