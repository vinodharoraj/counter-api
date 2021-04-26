package com.search.count.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

public class AppUtil {

	public static String inputFileName = "inputFile.txt";

	public static File getFile() {
		File inputFile = null;
		URL resource = AppUtil.class.getClassLoader().getResource(inputFileName);
		try {
			inputFile = new File(resource.toURI());
		} catch (URISyntaxException e) {

		}
		return inputFile;
	}
	
	public static String[] cleanInputFile(File inputFile) throws IOException {
		return FileUtils.readFileToString(inputFile).replaceAll("[, .]", StringUtils.SPACE).split("\\s+");
	}

}
