package com.parul.utils.service;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class WebsiteExtractor {
	
	void getDocument(String url) throws IOException {
		Document doc = Jsoup.parse(url);
		doc.getAllElements().stream().forEach(e -> System.out.println(e.getAllElements()));
	}
	
	public static void main(String[] args) throws IOException {
		WebsiteExtractor extractor = new WebsiteExtractor();
		extractor.getDocument("https://elevenlabs.io/");
	}

}
