package com.janak;
import java.util.*;
public class WebCrawler {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter URL");
        String url = scanner.nextLine();
        CrawlHelper.crawl(url, url);
    }
}