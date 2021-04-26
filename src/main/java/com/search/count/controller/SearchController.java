package com.search.count.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.search.count.model.SearchText;
import com.search.count.util.AppUtil;

@RestController
@RequestMapping("/search")
public class SearchController {
	static Logger logger = Logger.getLogger(SearchController.class.getName());
	
	
	

	@PostMapping
	public ResponseEntity<String> hello(@RequestBody String inputJson) {
		File inputFile = AppUtil.getFile();
		List<Map<String, Integer>> countsList = new ArrayList<>();
		List<String> inputList = getStringList(inputJson);
		Map<String, Integer> splittedMap = new HashMap<String, Integer>();
		Map<String, List<Map<String, Integer>>> counts = new HashMap<>();
		try {
			String[] fileLines = AppUtil.cleanInputFile(inputFile);
			splittedMap = getMatchingString(fileLines, inputList);
		} catch (IOException i) {
			logger.info("An IO Exception occurred while reading the file.");
		}
		Map<String, Integer> sortedMap = splittedMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		countsList.add(sortedMap);
		counts.put("counts", countsList);
		String bodyJSON = prepareJSONStructure(counts);
		return new ResponseEntity<String>(bodyJSON, HttpStatus.OK);
	}

	private Map<String, Integer> getMatchingString(String[] fileLines, List<String> inputList) {
		Map<String,Integer> countWords = countWords(fileLines);
		return countWords.entrySet().stream().filter(p -> verifyMatchingString(p.getKey(), inputList))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	private boolean verifyMatchingString(String key, List<String> inputList) {
		return inputList.stream().anyMatch(key::contains);
	}

	private Map<String, Integer> countWords(String[] fileLines) {
		return (Arrays.asList(fileLines)).parallelStream()
				.collect(Collectors.toConcurrentMap(word -> word, word -> 1, Integer::sum)).entrySet().stream()
				.sorted(Collections.reverseOrder(Comparator.comparing(Entry::getValue)))
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	private String prepareJSONStructure(Map<String, List<Map<String, Integer>>> sortedMapList) {
		String bodyJSON = StringUtils.EMPTY;
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		om.setSerializationInclusion(Include.NON_EMPTY);
		om.setSerializationInclusion(Include.NON_ABSENT);
		om.setSerializationInclusion(Include.NON_DEFAULT);
		try {
			bodyJSON = om.writeValueAsString(sortedMapList);
		} catch (JsonProcessingException e) {

		}
		return bodyJSON;
	}

	private List<String> getStringList(String inputJson) {
		List<String> inputList = new ArrayList<String>();
		ObjectMapper om = new ObjectMapper();
		try {
			SearchText counts = om.readValue(inputJson, SearchText.class);
			inputList = counts.getSearchText();
			System.out.println(counts);
		} catch (JsonMappingException jm) {

		} catch (JsonProcessingException jp) {

		}
		return inputList;
	}

}
