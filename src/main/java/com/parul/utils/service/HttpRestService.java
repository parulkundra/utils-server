package com.parul.utils.service;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;
import java.util.function.Consumer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HttpRestService {
	
	private final RestTemplate restTemplate;
	
	public <T> T callPostService(Class<T> t, String url, String payload, Consumer<HttpHeaders> headers) {
		log.info("Calling {}" , url);
		return RestClient.builder().build()
				.post()
				.uri(URI.create(url)).body(payload)
				.headers(headers)
				.contentType(MediaType.APPLICATION_JSON)
				.retrieve().body(t);
	}
	
	public <T> T callGetService(Class<T> t, String url, Consumer<HttpHeaders> headers) {
		log.info("Calling {}" , url);
		return RestClient.builder().build()
				.get()
				.uri(URI.create(url))
				.headers(headers)
				.retrieve().body(t);
	}
	
	public <T> T callPostService(Class<T> t, Object payload, String url, HttpHeaders headers) {
		log.info("Calling {}" , url);
		HttpEntity requestEntity = new HttpEntity(payload, headers);
		ResponseEntity<T> response = restTemplate.exchange( url, HttpMethod.POST, requestEntity, t);
		return response.getBody();
	}
	
	public <T> T callGetService(Class<T> t, String url, HttpHeaders headers) {
		log.info("Calling {}" , url);
		HttpEntity requestEntity = new HttpEntity<>(headers);
		ResponseEntity<T> response = restTemplate.exchange( url, HttpMethod.GET, requestEntity, t);
		return response.getBody();
	}
	
	public String getPage(String url, String... headers) throws IOException, InterruptedException {
		log.info("Calling {}" , url);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										.headers(headers)
										.uri(URI.create(url)).build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		return response.body();
	}
	
	public String getPage(String url) throws IOException, InterruptedException {
		log.info("Calling {}" , url);
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
										.uri(URI.create(url)).build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

		return response.body();
	}
	
//	public String getPage(String url) {
//		log.info("Calling {}" , url);
//		String content = null;
//		URLConnection connection = null;
//		try {
//		  connection =  new URL(url).openConnection();
//		  Scanner scanner = new Scanner(connection.getInputStream());
//		  scanner.useDelimiter("\\Z");
//		  content = scanner.next();
//		  scanner.close();
//		}catch ( Exception ex ) {
//		    ex.printStackTrace();
//		}
//		return content;
//	}
	
}
