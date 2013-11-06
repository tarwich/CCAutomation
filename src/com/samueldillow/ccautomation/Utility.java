package com.samueldillow.ccautomation;

public class Utility {
	public static Object at(int index, Object[] array) {
		try {
			// Cast the item to integer and return
			return array[index];
		} catch(Exception ignore) {
			return null;
		}
	}
	
	public static Boolean cast(Object in, Boolean defaultValue) {
		try {
			return (Boolean) in;
		} catch(Exception ignore) { }
		
		try {
			return Boolean.parseBoolean((String) in);
		} catch(Exception ignore) {}
		
		return defaultValue;
	}
	
	public static Integer cast(Object in, Integer defaultValue) {
		try {
			return (Integer) in;
		} catch(Exception ignore) {}
		
		try {
			return Integer.parseInt((String) in);
		} catch(Exception ignore) { }
		
		return defaultValue;
	}

	public static String cast(Object in, String defaultValue) {
		try {
			return (String) in;
		} catch(Exception ignore) {}
		
		try {
			return in.toString();
		} catch(Exception ignore) {}
		
		return defaultValue;
	}
}
