package com.mylotto.analysis;

import com.mylotto.helper.StringUtils;

/**
 * Query to perform number analysis.
 * 
 * @author MEKOH
 *
 */
public final class NumberAnalysisQuery {

	public String matchedNo = StringUtils.EMPTY;
	public String lotto = StringUtils.EMPTY;
	public int noOfDraw = 10;
	public boolean matchExact = false;
	
	/**
	 * Constructor
	 * 
	 * @param drawNo
	 * @param lotto
	 * @param noOfDraw
	 * @param matchExact
	 */
	public NumberAnalysisQuery(String drawNo, String lotto, int noOfDraw, boolean matchExact){
		this.matchedNo = drawNo;
		this.lotto = lotto;
		this.noOfDraw = noOfDraw;
		this.matchExact = matchExact;
	}

	@Override
	public String toString() {
		return "NumberAnalysisQuery [matchedNo=" + matchedNo + ", lotto=" + lotto + ", noOfDraw=" + noOfDraw + ", matchExact=" + matchExact + "]";
	}
}
