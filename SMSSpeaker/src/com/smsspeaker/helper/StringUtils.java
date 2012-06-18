package com.smsspeaker.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * String helper class.
 * 
 */
public final class StringUtils {

	public static final String EMPTY = "";
	
	public static boolean isNullorEmpty(final String value) {
		if (value == null || EMPTY.equals(value)){
			return true;
		}
		return false;
	}
	
	public static String removeNull(final String value) {
		if (value == null || EMPTY.equals(value)){
			return StringUtils.EMPTY;
		}
		return value;
	}
	
	public static String inputStreamToString(final InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        return sb.toString();
    }
}
