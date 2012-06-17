package com.mylotto.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.mylotto.R;
import com.mylotto.data.PrizeType;
import com.mylotto.data.Result4D;
import com.mylotto.helper.DateUtils;
import com.mylotto.helper.StringUtils;

/**
 * Number analysis implementation.
 * 
 * @author MEKOH
 * 
 */
public final class NumberAnalysisImpl extends BaseAnalysis {

	private final NumberAnalysisQuery query;

	/**
	 * Constructor
	 * 
	 * @param query
	 *            The query used for analysis
	 */
	public NumberAnalysisImpl(final NumberAnalysisQuery query) {
		this.query = query;
	}

	/**
	 * Perform number analysis.
	 * 
	 * @param pastResults
	 * @return Analysis results
	 */
	public NumberAnalysisResults perform(final List<Result4D> pastResults) {
		NumberAnalysisResults results = new NumberAnalysisResults(query);
		int count = 0;
		String status = StringUtils.EMPTY;
		String message = listener.getMessage(R.string.message_analyze_draw);

		results.totalDraws = pastResults.size();
		if (pastResults.size() > 0) {
			Date firstDate = pastResults.get(0).drawDate;
			Date lastDate = pastResults.get(pastResults.size() - 1).drawDate;

			if (firstDate.before(lastDate)) {
				results.from = firstDate;
				results.until = lastDate;
			} else {
				results.from = lastDate;
				results.until = firstDate;
			}
		}
		for (Result4D result4D : pastResults) {
			count++;
			status = String.format(message, count, pastResults.size(), result4D.drawNo, DateUtils.formatDate(result4D.drawDate));
			listener.notifyStatus(status);

			verifyResults(results, result4D, PrizeType.FIRST_PRIZE, result4D.firstPrize);
			verifyResults(results, result4D, PrizeType.SECOND_PRIZE, result4D.secondPrize);
			verifyResults(results, result4D, PrizeType.THIRD_PRIZE, result4D.thirdPrize);

			for (String no : result4D.specialNumbers) {
				verifyResults(results, result4D, PrizeType.SPECIAL, no);
			}
			for (String no : result4D.consolationNumbers) {
				verifyResults(results, result4D, PrizeType.CONSOLATION, no);
			}
		}

		if (results.matchedDraws.size() > 0) {
			results.totalMatched = results.matchedDraws.size();

			Date firstDate = results.matchedDraws.get(0).drawDate;
			Date lastDate = results.matchedDraws.get(results.matchedDraws.size() - 1).drawDate;
			if (firstDate.before(lastDate)) {
				results.firstMatchedOn = firstDate;
				results.lastMatchedOn = lastDate;
			} else {
				results.firstMatchedOn = lastDate;
				results.lastMatchedOn = firstDate;
			}

			// Derive the gaps, average draws per gap, and predicted next draw
			Collections.sort(results.matchedDraws, new Comparator<MatchedDraw>() {
				public int compare(MatchedDraw m1, MatchedDraw m2) {
					return m1.drawDate.compareTo(m2.drawDate);
				}
			});

			Collections.sort(pastResults, new Comparator<Result4D>() {
				public int compare(Result4D m1, Result4D m2) {
					return m1.drawDate.compareTo(m2.drawDate);
				}
			});

			List<Integer> matchIndexes = new ArrayList<Integer>(3);
			for (MatchedDraw matchedDraw : results.matchedDraws) {
				for (int i = 0; i < pastResults.size() - 1; i++) {
					if (pastResults.get(i).drawDate.equals(matchedDraw.drawDate)) {
						matchIndexes.add(i);
					}
				}
			}

			if (matchIndexes.size() > 1) {

				// Derive the gaps
				for (int i = 0; i < matchIndexes.size() - 1; i++) {
					results.gaps.add(matchIndexes.get(i + 1) - matchIndexes.get(i) - 1);
				}

				// Derive average draw gaps
				int gapSum = 0;
				for (Integer gap : results.gaps) {
					gapSum += gap;
				}
				results.averageGapBetweenDraws = gapSum / results.gaps.size();

				// Derive the next draw
				results.predictedNextDraw = pastResults.size() - matchIndexes.get(matchIndexes.size() - 1)  + (int)results.averageGapBetweenDraws;
			}
		}

		return results;
	}

	/**
	 * 
	 * @param compareDate
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	private boolean isDateWithin(final Date compareDate, final Date fromDate, final Date toDate) {
		if (compareDate == null || fromDate == null || toDate == null)
			return false;

		if (compareDate.after(fromDate) && compareDate.before(toDate))
			return true;
		return false;
	}

	/**
	 * Validate the draw results against the number.
	 * 
	 * @param results
	 * @param result4D
	 * @param prizeType
	 * @param matchedNo
	 */
	private void verifyResults(final NumberAnalysisResults results, final Result4D result4D, final String prizeType, final String matchedNo) {
		boolean isTop3 = AnalysisHelper.isTop3Prize(prizeType);
		if (!query.matchExact) {
			if (isMatchRandom(query.matchedNo, matchedNo)) {
				boolean isMatchExact = isMatchExact(query.matchedNo, matchedNo);
				addMatchedDraw(results, buildMatchedDraw(result4D, prizeType, matchedNo, isMatchExact));
				results.totalRandomMatched = results.totalRandomMatched + 1;
				if (isTop3) {
					results.totalRandomMatchTop3 = results.totalRandomMatchTop3 + 1;
				}
				if (isMatchExact) {
					results.totalExactMatched = results.totalExactMatched + 1;
					if (isTop3) {
						results.totalExactMatchTop3 = results.totalExactMatchTop3 + 1;
					}
				}
			}
		} else {
			if (isMatchExact(query.matchedNo, matchedNo)) {
				addMatchedDraw(results, buildMatchedDraw(result4D, prizeType, matchedNo, true));
				results.totalExactMatched = results.totalExactMatched + 1;
				if (isTop3) {
					results.totalExactMatchTop3 = results.totalExactMatchTop3 + 1;
				}
			}
		}
	}

	/**
	 * @param results
	 * @param matchedDraw
	 */
	private void addMatchedDraw(final NumberAnalysisResults results, final MatchedDraw matchedDraw) {
		results.matchedDraws.add(matchedDraw);
	}
}
