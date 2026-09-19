# rotwatch

A command line link checker. Point it at a website and it crawls the site,
follows every link it finds, and reports which ones are dead and where they live.


## Why

A site with a few years of history accumulates broken links quietly, and there is no cheap way
to find them without clicking through everything by hand.

rotwatch does that walk for you and tells you which page each broken link sits on,
so fixing them is a matter of editing files rather than hunting.

## What it does

Given a starting URL, rotwatch fetches the page, extracts every link, and then

- checks whether each link is reachable
- crawls further into any link on the same domain
- checks links to other domains without crawling them
- reports broken links grouped by the page they were found on

Statuses are reported separately, so a 404 or a connection failure counts as broken,
while a redirect is reported but not treated as an error by default.

## Usage

```
rotwatch https://example.com
```

Options

```
--depth <n>          how many levels to crawl from the seed (default 5)
--concurrency <n>    parallel requests (default 16)
--timeout <ms>       per request timeout (default 10000)
--rate <n>           max requests per second per host (default 4)
--json <path>        write a machine readable report
--fail-on-redirect   treat redirects as failures
```

rotwatch exits with a non-zero status when broken links are found, so it can be
used as a check in CI.

## Behaviour

rotwatch respects `robots.txt` and rate limits itself per host. Requests that fail
transiently are retried with backoff before being reported as broken. A URL that
appears on many pages is fetched once and reported against every page it appears on.

## Building

Requires Java 21.

```
./gradlew build
./gradlew run --args="https://example.com"
```

## Design

Notes on the significant decisions, and the alternatives that were rejected, live in
[docs/decisions](docs/decisions).

## Licence

MIT
