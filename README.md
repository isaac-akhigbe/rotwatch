# rotwatch

A concurrent link checker for websites, written in Java 21.

Point it at a site, and it crawls every page on that site, finds every link, and checks each one. It reports broken links, redirects, and network failures.

```
$ rotwatch https://isaacakhigbe.xyz/
200 https://isaacakhigbe.xyz/
200 https://cloover.co
301 https://market-connect.net/
404 https://github.com/bourdillion
301 https://www.linkedin.com/in/isaacakhigbe22/
200 https://x.com/bourdilion01
403 https://medium.com/@bourdillion/...
crawled in 1647ms
checked 17 links in 5757ms
```

The first real run found a broken link on its own author's website. The `404` above is a GitHub profile link left over from an old username.

## Features

- Crawls every page on the starting site, visiting each page once
- Checks links to other sites without crawling them
- Reports redirects (`301`) instead of silently following them
- Reports network failures as `ERROR` lines instead of crashing
- Checks links concurrently using Java 21 virtual threads
- Sends at most 4 requests at a time to any one host
- Identifies itself with a `User-Agent` header
- Skips non-web links like `mailto:` and `tel:`
- Ignores `#fragment` duplicates

## Install

Requires Java 21.

```
git clone https://github.com/isaac-akhigbe/rotwatch.git
cd rotwatch
./gradlew installDist
```

The command is now at `app/build/install/rotwatch/bin/rotwatch`. To run it from anywhere, link it into a folder on your `PATH`.

```
ln -s "$PWD/app/build/install/rotwatch/bin/rotwatch" ~/.local/bin/rotwatch
```

## Usage

```
rotwatch <url>
```

Results go to standard output, one line per link. Timing goes to standard error, so you can save just the results.

```
rotwatch https://example.com/ > report.txt
```

## How it works

```
Crawler        visits each page on the site, breadth first
  PageFetcher    downloads a page's HTML
  LinkExtractor  finds links with jsoup and resolves them to full URLs
LinkChecker    checks each link concurrently, limited per host
App            prints a result line per link
```

Each check returns a `CheckResult`, a sealed interface that is either a `Response` with a status code or a `Failure` with a reason. `App` handles both with an exhaustive `switch`.

## Performance

Checking the 17 links on `isaacakhigbe.xyz`.

| Approach | Time |
|---|---|
| One at a time | 18 to 22 s |
| Concurrent | about 6 s |

The concurrent run takes about as long as the slowest single link. Measured on a home connection, so expect variation.

## Testing

```
./gradlew build
```

Tests run against a local fake HTTP server, so they need no internet connection and don't depend on any real website. CI runs them on every pull request.

## Known limitations

- Some sites block non-browser clients and answer `403` even when the page works
- On slow networks, the 10 second connect timeout can report working links as errors
- The start page doesn't follow redirects

## License

MIT