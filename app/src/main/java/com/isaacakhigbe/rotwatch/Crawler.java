package com.isaacakhigbe.rotwatch;

import java.net.URI;

public class Crawler {
    boolean isSameSite(URI start, URI link) {
        String startHost = start.getHost();
        String linkHost = link.getHost();
        return startHost.equalsIgnoreCase(linkHost);
    }
}
