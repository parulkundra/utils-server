package com.parul.utils.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class AssToTextConverter {
	
	private static final String ASS_FILE = "C:\\development\\python_projects\\Karaoke\\pankhi\\pankhi.ass";
	private static final String TEMP_DATE = "2026-01-01T";
	public static void main(String[] args) throws IOException, ParseException {
//		SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.ss");
//		SimpleDateFormat sdfTimeFormat = new SimpleDateFormat("mm:ss.ss");
		DateTimeFormatter sdfTimeFormat = DateTimeFormatter.ofPattern("mm:ss.SSS").withZone(ZoneId.systemDefault());
		List<String> assLines = FileUtils.readLines(new File(ASS_FILE));
		
		List<String> output = new ArrayList<String>();
		
		for (String assLine : assLines ) {
			try {
				if (assLine.startsWith("Dialogue")) {
					String start = normalizeDate(assLine.split(",")[1]);
					Instant startTime = Instant.parse(TEMP_DATE + start + "Z");
	//				Date endTime =  sdf.parse(assLine.split(",")[2].substring(2) + "0");
					Instant interimTime =  Instant.parse(TEMP_DATE + start + "Z");
					String lineOutput = "";
					String[] assSplit = assLine.split("\\{");
					for (int i = 1; i < assSplit.length; i++) {
						String assWord = assSplit[i];
						String[] braceSplit = assWord.replace(",", "").replace(".", "").split("}");
						assWord = braceSplit[1].trim();
	//					String assTiming = braceSplit[0].replace("\\k", "").trim();
						String formatted = null;
						if (i == 1) {
							formatted = sdfTimeFormat.format(startTime);
							lineOutput = "[" + formatted.substring(0, formatted.length()-1) + "]" + assWord;
						} else {
							String lastAssWord = assSplit[i-1];
							String[] braceLastSplit = lastAssWord.replace(",", "").replace(".", "").split("}");
	//						lastAssWord = braceLastSplit[1].trim();
							Long assLastTiming = Long.valueOf(braceLastSplit[0].replace("\\k", "").trim() + "0");
							interimTime = interimTime.plusMillis(assLastTiming);
							formatted = sdfTimeFormat.format(interimTime);
							lineOutput = lineOutput +  " [" + formatted.substring(0, formatted.length()-1) + "]" + assWord;
						}
					}
					output.add(lineOutput);
				}
			} catch (Exception e) {
				System.err.println(assLine);
			}
		}
		output.forEach(e -> System.err.println(e));
	}
	private static String normalizeDate(String date) {
		String output = "";
		String[] split = date.split(":");
		for (String splitWord: split) {
			if (splitWord.length() == 1) {
				output = output + "0" + splitWord + ":";
			} else if (splitWord.contains(".")) {
				String[] subSplit = splitWord.split("\\.");
				for (int i = 0; i < subSplit.length; i++) {
					String subSplitWord = subSplit[i];
					if (i == 0) {
						if (subSplitWord.length() == 1) {
							output = output + "0" + subSplitWord;
						} else  {
							output = output + subSplitWord;
						}
					} else {
						if (subSplitWord.length() == 1) {
							output = output + "." + "0" + subSplitWord;
						} else  {
							output = output + "." + subSplitWord;
						}
					}
				}
			} else {
				output = output + splitWord + ":";
			}
		}
		return output;
	}
}
