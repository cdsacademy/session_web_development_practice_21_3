package com.cds.academy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


public class Tester {
	
	@Test
	public void teeest() {
		String message =" hola #asdfsad";
		Pattern regex = Pattern.compile(".*?\\s(#\\w+).*?");
		Matcher regexMatcher = regex.matcher(message);
		
		List<String> allMatches = new ArrayList<String>();
		while(regexMatcher.find()) {
			allMatches.add(regexMatcher.group());
		}
		
		System.out.println(allMatches);
	}

}
