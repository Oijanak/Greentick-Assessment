# Web Vulnerability Scanner

## Brief Description

The Web Vulnerability Scanner is a tool that crawls a given website URL to identify common security vulnerabilities. The scanner checks for missing HTTP security headers, outdated software versions, and insecure forms. It recursively follows all the internal links of the provided URL to perform a thorough scan of the website's pages. The tool uses the Jsoup library for HTML parsing and Java's `HttpClient` for making HTTP requests.

## Features:

- **Security Header Check**: Verifies the presence of essential HTTP security headers such as `Strict-Transport-Security`, `X-Content-Type-Options`, `X-Frame-Options`, and `Content-Security-Policy`.
- **Outdated Software Version Detection**: Checks for outdated versions of popular web servers and platforms like Apache, Nginx, PHP, Node.js, and more.
- **Form Security Check**: Identifies forms that lack an `action` attribute or use insecure methods (such as `GET`).
- **Recursive Crawling**: Crawls through all internal links within the base URL to perform an extensive scan.

## Steps to Run the Code

### 1. Go to Project directory

```bash
cd WebScanCrawler

```

### 2. Install Dependencies and run main class

For Unix/Mac

```bash
./mvnw clean install
./mvnw exec:java -Dexec.mainClass="com.janak.WebCrawler"

```

For Window
For Unix/Mac

```bash
mvnw.cmd clean install
mvnw.cmd exec:java -Dexec.mainClass="com.janak.WebCrawler"

```

## Assumptions

- The scanner assumes the server will respond to HTTP HEAD requests to retrieve headers and software version information.
- It uses predefined patterns to detect outdated versions of popular web servers and platforms.
- The tool uses the Jsoup library to parse the HTML and extract internal links for crawling.
