package com.drexel.cs575.credibilityassessor.util;

import java.util.HashMap;
import java.util.StringTokenizer;

public class TextTokenizer {
	
	/**
	 * Converts a text string into a hashmap where they keys are unique words and the values are frequencies for each
	 * @param text
	 * @return
	 */
	public static HashMap<String,Integer> tokenizeString(String text) {
		HashMap<String,Integer> map = new HashMap<String,Integer>();
		
		StringTokenizer st = new StringTokenizer(text);
		st.countTokens();
	     while (st.hasMoreTokens()) {
	         String token = st.nextToken();
	         
	         if(map.containsKey(token)) {//if we have already seen the word, increment the count
	        	 map.put(token, map.get(token)+1);
	        	 
	         } else {//otherwise, add it to the hash
	        	 map.put(token, 1);
	        	 
	         }
	     }
		
		return map;
	}
	
	/**
	 * Produces the ratio of repeat to unique words in the string 
	 * @param text
	 * @return
	 */
	public static double deriveTypeTokenRatio(String text) {
		HashMap<String,Integer> map = tokenizeString(text);
		
		StringTokenizer st = new StringTokenizer(text);
		Integer numTokens = st.countTokens();
		
		return (map.keySet().size() / numTokens.doubleValue());
	}

}
