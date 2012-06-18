package com.mylotto.analysis;

import java.util.Date;

import com.mylotto.helper.StringUtils;

/**
 * Frequency analysis query.
 * 
 * @author MEKOH
 *
 */
public final class FrequencyAnalysisQuery {

	public String lotto = StringUtils.EMPTY;
	public int noOfDraw = 10;
	public String fromDate;
	public String toDate;

	/**
	 * Constructor
	 * 
	 * @param lotto
	 * @param noOfDraw
	 * @param fromDate
	 * @param toDate
	 */
	public FrequencyAnalysisQuery(String lotto, final int noOfDraw, final String fromDate, final String toDate)  {
		super();
		this.lotto = lotto;
		this.noOfDraw = noOfDraw;
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "FrequencyAnalysisQuery [lotto=" + lotto + ", noOfDraw=" + noOfDraw + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}

	
	

}
