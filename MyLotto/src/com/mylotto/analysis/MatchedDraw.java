package com.mylotto.analysis;

import java.util.Date;

public final class MatchedDraw {

	public final String drawNo;
	public final Date drawDate;
	public final String drawDay;
	public final String prizeType;
	public final String matchedNo;
	public final boolean exactMatch;

	/**
	 * Constructor
	 * 
	 * @param drawNo
	 * @param drawDate
	 * @param drawDay
	 * @param prizeType
	 * @param matchedNo
	 * @param exactMatch
	 */
	public MatchedDraw(final String drawNo, final Date drawDate, final String drawDay, final String prizeType, final String matchedNo, final boolean exactMatch) {
		super();
		this.drawNo = drawNo;
		this.drawDate = drawDate;
		this.drawDay = drawDay;
		this.prizeType = prizeType;
		this.matchedNo = matchedNo;
		this.exactMatch = exactMatch;
	}
}
