package com.mylotto.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Analysis results
 * 
 * @author MEKOH
 *
 */
public final class NumberAnalysisResults {

	public Date from;
	public Date until;
	public Date firstMatchedOn;
	public Date lastMatchedOn;
	public int totalDraws = 0;
	public int totalMatched = 0;
	public int totalExactMatched = 0;
	public int totalRandomMatched = 0;
	public int totalExactMatchTop3 = 0;
	public int totalRandomMatchTop3 = 0;
	public double averageGapBetweenDraws = 0;
	public int predictedNextDraw = 0;
	public final List<Integer> gaps = new ArrayList<Integer>();
	public final List<MatchedDraw> matchedDraws = new ArrayList<MatchedDraw>(3);
	public final NumberAnalysisQuery query;

	/**
	 * Constructor.
	 * 
	 * @param query
	 */
	public NumberAnalysisResults(NumberAnalysisQuery query) {
		this.query = query;
	}

}
