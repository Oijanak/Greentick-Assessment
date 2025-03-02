package com.janak;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlHelper {
    private static final String[] SECURITY_HEADERS = {
            "Strict-Transport-Security",
            "X-Content-Type-Options",
            "X-Frame-Options",
            "Content-Security-Policy"
    };

    private static final Map<String, Integer> OUTDATED_VERSIONS = new HashMap<>() {{
        put("Apache", 2);
        put("nginx", 2);
        put("PHP", 8);
        put("Node.js", 16);
        put("Tomcat", 9);
        put("Express", 4);
        put("WordPress", 6);
        put("Django", 3);
    }};


    private static final String[] VERSION_PATTERNS = {
            "Apache/(\\d+\\.\\d+\\.\\d+)",
            "nginx/(\\d+\\.\\d+\\.\\d+)",
            "PHP/(\\d+\\.\\d+\\.\\d+)",
            "Node.js/(\\d+\\.\\d+\\.\\d+)",
            "Tomcat/(\\d+\\.\\d+\\.\\d+)",
            "Express/(\\d+\\.\\d+\\.\\d+)",
            "WordPress/(\\d+\\.\\d+\\.\\d+)",
            "Django/(\\d+\\.\\d+\\.\\d+)"
    };


    private static final String[] HEADERS_TO_CHECK = {
            "Server", "X-Powered-By", "X-Backend-Server", "X-Generator"
    };

    private static final Set<String> visitedUrls = new HashSet<>();

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static void crawl(String url, String baseUrl) {
        if (visitedUrls.contains(url)) {
            return;
        }
        visitedUrls.add(url);
        try {
            Document document = Jsoup.connect(url).get();
            System.out.println("VULNERABILITY SCAN REPORT FOR " + url + ":");
            checkSecurityHeaders(url);
            checkSoftwareVersions(url);
            checkForms(document, url);
            System.out.println();

            Elements links = document.select("a[href]");
            for (Element link : links) {
                String nextUrl = link.attr("abs:href");
                if (nextUrl.startsWith(baseUrl)) {
                    crawl(nextUrl, baseUrl);
                }
            }
        } catch (IOException e) {
            System.err.println("Error crawling " + url + ": " + e.getMessage());
        }
    }

    private static void checkSecurityHeaders(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            for (String header : SECURITY_HEADERS) {
                if (!response.headers().map().containsKey(header)) {
                    System.out.println("- MISSING HTTP SECURITY HEADER: " + header);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error checking headers for " + url + ": " + e.getMessage());
        }
    }

    private static void checkSoftwareVersions(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            for (String headerName : HEADERS_TO_CHECK) {
                response.headers().firstValue(headerName).ifPresent(CrawlHelper::detectAndReportOutdatedVersions);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error checking software versions for " + url + ": " + e.getMessage());
        }
    }

    private static void detectAndReportOutdatedVersions(String headerValue) {
        for (String pattern : VERSION_PATTERNS) {
            Matcher matcher = Pattern.compile(pattern).matcher(headerValue);
            if (matcher.find()) {
                String software = pattern.split("/")[0];
                String detectedVersion = matcher.group(1);

                if (OUTDATED_VERSIONS.containsKey(software)) {
                    int latestMajorVersion = OUTDATED_VERSIONS.get(software);
                    if (isOutdated(detectedVersion, latestMajorVersion)) {
                        System.out.println("- OUTDATED SOFTWARE VERSION DETECTED: " + software + " " + detectedVersion);
                    }
                }
            }
        }
    }

    private static boolean isOutdated(String detectedVersion, int latestMajorVersion) {
        int detectedMajorVersion = Integer.parseInt(detectedVersion.split("\\.")[0]);
        return detectedMajorVersion < latestMajorVersion;
    }

    private static void checkForms(Document document, String url) {
        Elements forms = document.select("form");
        for (Element form : forms) {
            String action = form.attr("action");
            String method = form.attr("method").toUpperCase();

            if (action.isEmpty()) {
                System.out.println("- FORM WITHOUT ACTION ATTRIBUTE: " + url);
            }
            if (method.equals("GET")) {
                System.out.println("- FORM WITH INSECURE METHOD (GET): " + url);
            }
        }
    }
}
