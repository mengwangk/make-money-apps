package com.mylotto.analysis;

import java.util.ArrayList;
import java.util.List;

import com.mylotto.data.Result4D;

/**
 * Base class for all analysis algorithm implementations.
 * 
 * @author MEKOH
 *
 */
public abstract class BaseAnalysis {
	
	
	
	/**
	 * Event listener
	 */
	public IAnalysisListener listener;

	/**
	 * Check if 2 numbers match randomly
	 * 
	 * @param number1
	 * @param number2
	 * @return
	 */
	protected boolean isMatchRandom(String number1, String number2){
		char[] firstArr = number1.toCharArray();
		char[] secondArr = number2.toCharArray();
		List<Character> secondList = new ArrayList<Character>(secondArr.length);
		for (char c: secondArr){
			secondList.add(c);
		}
		for (char c: firstArr){
			if (secondList.contains(c)){
				secondList.remove((Character)c);
			}
		}
		return (secondList.size() == 0);
	}
	
	/**
	 * Check if 2 numbers match exactly the same
	 * 
	 * @param number1
	 * @param number2
	 * @return
	 */
	protected boolean isMatchExact(String number1, String number2){
		return (number1.equals(number2));
	}
	
	/**
	 * Build the MatchedDraw object
	 * 
	 * @param result4D
	 * @param prizeType
	 * @param matchedNo
	 * @param exactMatch
	 * @return
	 */
	protected MatchedDraw buildMatchedDraw(final Result4D result4D, final String prizeType, final String matchedNo, final boolean exactMatch) {
		return new MatchedDraw(result4D.drawNo, result4D.drawDate, result4D.drawDay, prizeType, matchedNo, exactMatch);
	}

}
