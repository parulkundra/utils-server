package com.parul.utils.service;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

public class WebsiteExtractor {
	
	private final String BASE_FOLDER = "C:/development/git/starter-server/src/main/resources/";
	private final String BASE_RESOURCE_RELATIVE_FOLDER = "assets";
	private final String BASE_RESOURCE_FOLDER = BASE_FOLDER + "static/" + BASE_RESOURCE_RELATIVE_FOLDER + "/";
	private final String BASE_TEMPLATE_FOLDER = BASE_FOLDER + "templates/";
	
	private HttpRestService httpRestService = new HttpRestService(new RestTemplate());
	
	void getDocument(String url, String name) throws Exception {
		String page = httpRestService.getPage(url);
		page = getUrls(page, "\\.css", ".css", "css");
		page = getUrls(page, "\\.js", ".js", "js");
		page = getUrls(page, "\\.jpg", ".jpg", "images");
		page = getUrls(page, "\\.jpeg", ".jpeg", "images");
		page = getUrls(page, "\\.webp", ".webp", "images");
		page = getUrls(page, "\\.png", ".png", "images");
		page = getUrls(page, "\\.mp3", ".mp3", "media");
		page = getUrls(page, "\\.mp4", ".mp4", "media");
		FileUtils.write(new File(BASE_TEMPLATE_FOLDER + name), page);
	}
	
	public String getUrls(String page, String type, String ext, String folder) throws Exception {
		String[] split = page.split(type);
		for (int i = 0; i < split.length-1; i++) {
			String line = split[i];
			int lastIndexOfHttps = line.lastIndexOf("https");
			if (lastIndexOfHttps != -1 && line.length() -lastIndexOfHttps < 300) {
				String filenamePath = (line.substring(lastIndexOfHttps, line.length()) + ext);
				System.out.println(filenamePath);
				filenamePath = filenamePath
						.replace(" ", "%20")
						.replace("%3A", ":")
						.replace("%2F", "/");
				System.out.println(filenamePath);
				String filename = filenamePath.substring(filenamePath.lastIndexOf("/"), filenamePath.length());
				System.out.println(filename);
				if (filenamePath.contains("thumbnailURL")) {
					String[] split2 = filenamePath.split("thumbnailURL");
					String url = split2[0].substring(0, split2[0].lastIndexOf("/"));
					String name = split2[1].substring(split2[1].lastIndexOf("/"));
					filenamePath = url + name;
					String resource = httpRestService.getPage(filenamePath);
					FileUtils.write(new File(BASE_RESOURCE_FOLDER + "/" + folder + filename), resource);
				} else {
					String resource = httpRestService.getPage(filenamePath);
					if (StringUtils.isNotBlank(resource)) {
						FileUtils.write(new File(BASE_RESOURCE_FOLDER + "/" + folder + filename), resource);
						page = page.replace(filenamePath, BASE_RESOURCE_RELATIVE_FOLDER + "/" + folder + filename);
					}
				}
			}
		}
		return page;
	}
	
	public static void main(String[] args) throws Exception {
		WebsiteExtractor extractor = new WebsiteExtractor();
		extractor.getDocument("https://elevenlabs.io/", "dashboard.html");
	}

}
