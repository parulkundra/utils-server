package com.parul.utils.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class AssDataCleanup {
	
	//not working
	
	private static final String ASS_FILE = "C:\\development\\python_projects\\Karaoke\\output.ass";
	private static final String LYRICS_FILE = "C:\\development\\python_projects\\Karaoke\\lyrics.txt";
	
	public static void main(String[] args) throws IOException {
		List<String> lyricsLines = FileUtils.readLines(new File(LYRICS_FILE));
		List<String> assLines = FileUtils.readLines(new File(ASS_FILE));
		
		List<String> cleanedUpAssLines = new ArrayList<String>();
		
		List<String> lyricsWords = new ArrayList<String>();
		for (String lyricsLine : lyricsLines) {
			String[] lyricsSplit = lyricsLine.split(" ");
			for (String lyricsWord : lyricsSplit) {
				lyricsWord = lyricsWord.replace(",", "").replace(".", "").trim();
				if (!lyricsWord.equals(",") && !lyricsWord.equals(".")) {
					lyricsWords.add(lyricsWord);
				}
			}
			lyricsWords.add("\n");
		}
		
		int lyricsCounter = 0;
		String extraWord = null;
		for (String assLine : assLines ) {
			if (assLine.startsWith("Dialogue")) {
				if (extraWord != null) {
					int index = assLine.indexOf("{");
					assLine = assLine.substring(0, index) + extraWord + " " + assLine.substring(index, assLine.length()); 
				}
				
				List<String> assWords = new ArrayList<String>();
				String[] assSplit = assLine.split("}");
				for (int i = 1; i < assSplit.length; i++) {
					String assWord = assSplit[i];
					if (assWord.contains("{\\")) {
						assWord = assWord.replace(",", "").replace(".", "").split("\\{")[0].trim();
						if (!assWord.equals(",") && !assWord.equals(".")) {
							assWords.add(assWord);
						}
					} else {
						assWord = assWord.replace(",", "").replace(".", "");
						if (!assWord.equals(",") && !assWord.equals(".")) {
							assWords.add(assWord);
						}
					}
				}
				
				for (int assCounter = 0 ; assCounter < assWords.size(); assCounter++) {
					String assWord = assWords.get(assCounter);
					String lyricsWord = lyricsWords.get(lyricsCounter);
					if (("\n").equals(lyricsWord) && assCounter < assWords.size() -1) {
						lyricsCounter++;
						assCounter--;
					} else if (assWord.equals(lyricsWord) && assCounter < assWords.size() -1) {
						lyricsCounter++;
					} else if (assWord.equals(lyricsWord) && assCounter == assWords.size() -1) {
						lyricsCounter++;
						if (lyricsCounter+1 < lyricsWords.size() && ("\n".equals(lyricsWords.get(lyricsCounter+1)))) {
							lyricsCounter++;
						}
					} else if (assCounter == assWords.size() -1) {
						lyricsCounter++;
						if (!("\n").equals(lyricsWord)) {
							String[] split = assLine.split(" ");
							assWord = split[split.length -1].replace(",", "");
						} else {
							assWord = null;
						}
					} else {
						System.err.println("mismatch");
					}
				}
				
			}
			cleanedUpAssLines.add(assLine);
		}
		cleanedUpAssLines.forEach(e -> System.err.println(e));
	}
	

}
