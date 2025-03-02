package com.janak;
import java.util.*;
public class WebCrawler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL");
        String url = scanner.nextLine();
        System.out.println("Scanning the web page "+url);
        CrawlHelper.crawl(url, url);
        scanner.close();
    }
}