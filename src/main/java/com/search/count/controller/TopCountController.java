package com.search.count.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.search.count.util.AppUtil;

@RestController
@RequestMapping("/top")
public class TopCountController {

	static Logger logger = Logger.getLogger(TopCountController.class.getName());

	@RequestMapping(value = "/{value}", method = RequestMethod.GET)
	public String hello(@PathVariable int value) {
		String result = StringUtils.EMPTY;
		if (value >= 0) {
			File inputFile = AppUtil.getFile();
			Map<String, Integer> mapSorted = new HashMap<String, Integer>();
			try {
				String[] fileLines = AppUtil.cleanInputFile(inputFile);
				mapSorted = countWords(fileLines, value);
			} catch (IOException i) {
				logger.info("An IO Exception occurred while reading the file.");
			}
			result = mapSorted.entrySet().stream().map(entry -> entry.getKey() + "|" + entry.getValue())
					.collect(Collectors.joining("\n"));
		} else {
			logger.info("Invalid Input value:: " + value);
		}
		return result;
	}

	private Map<String, Integer> countWords(String[] fileLines, int inputLimit) {
		return (Arrays.asList(fileLines)).parallelStream()
				.collect(Collectors.toConcurrentMap(word -> word, word -> 1, Integer::sum)).entrySet().stream()
				.sorted(Collections.reverseOrder(Comparator.comparing(Entry::getValue))).limit(inputLimit)
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

}