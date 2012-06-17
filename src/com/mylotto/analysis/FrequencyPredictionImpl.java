package com.mylotto.analysis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mylotto.R;
import com.mylotto.analysis.FrequencyAnalysisResults.DigitPosition;
import com.mylotto.analysis.FrequencyAnalysisResults.NumberFrequency;
import com.mylotto.analysis.FrequencyAnalysisResults.PairingFrequency;
import com.mylotto.analysis.FrequencyAnalysisResults.Prediction;
import com.mylotto.analysis.FrequencyAnalysisResults.SumofDigits;
import com.mylotto.data.PrizeType;
import com.mylotto.data.Result4D;
import com.mylotto.helper.DateUtils;
import com.mylotto.helper.StringUtils;

/**
 * @author MEKOH
 * 
 */
public final class FrequencyPredictionImpl extends BaseAnalysis {

	private final FrequencyAnalysisQuery query;

	/**
	 * Constructor
	 * 
	 * @param query
	 *            The query used for analysis
	 */
	public FrequencyPredictionImpl(final FrequencyAnalysisQuery query) {
		this.query = query;
	}

	/**
	 * Perform number analysis.
	 * 
	 * @param pastResults
	 * @return Analysis results
	 */
	public FrequencyAnalysisResults perform(final List<Result4D> pastResults, final List<Result4D> unUsedResults) {
		FrequencyAnalysisResults results = new FrequencyAnalysisResults(query);
		int count = 0;
		String status = StringUtils.EMPTY;
		String message = listener.getMessage(R.string.message_analyze_draw);

		// Set from and until dates
		if (pastResults.size() > 0) {

			results.totalDraws = pastResults.size();

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

			// Derive the position frequency
			checkPositionFrequency(results, result4D);

			// Derive the pairing frequency
			checkPairingFrequency(results, result4D);

			// Derive sum of digits
			checkSumofDigits(results, result4D);
		}

		// Frequency analysis
		analyzeNumberFrequency(results);

		// Sum of digits analysis
		analyzeSumofDigits(results);

		// Trend analysis
		for (Result4D result4D : pastResults) {
			count++;
			status = listener.getMessage(R.string.message_prediction_analysis);
			listener.notifyStatus(status);
			analyzeTrend(results, result4D);
		}

		// predict(results);
		// checkMatches(results, unUsedResults);

		return results;
	}

	// //////////////////////// Trend analysis
	// ///////////////////////////////////////////

	/**
	 * Trend analysis for each draw based on gathered statistics.
	 * 
	 * @param results
	 * @param result4D
	 */
	private final void analyzeTrend(final FrequencyAnalysisResults results, Result4D result4D) {

		//

	}

	// ///////////////////////////////////////////////////////////////////////////////////

	// ////////////////////////Start Prediction
	// ///////////////////////////////////////////

	private final void predict(final FrequencyAnalysisResults results) {

		listener.notifyStatus(listener.getMessage(R.string.message_check_top_pairings));
		getTopPairings(results, 0, results.zeroPairingFreq);
		getTopPairings(results, 1, results.onePairingFreq);
		getTopPairings(results, 2, results.twoPairingFreq);
		getTopPairings(results, 3, results.threePairingFreq);
		getTopPairings(results, 4, results.fourPairingFreq);
		getTopPairings(results, 5, results.fivePairingFreq);
		getTopPairings(results, 6, results.sixPairingFreq);
		getTopPairings(results, 7, results.sevenPairingFreq);
		getTopPairings(results, 8, results.eightPairingFreq);
		getTopPairings(results, 9, results.ninePairingFreq);

		List<Integer> fourthPosFreqs = getTopFreqDigits(results, DigitPosition.FOURTH);
		// List<Integer> thirdPosFreqs = getTopFreqDigits(results,
		// DigitPosition.THIRD);
		// List<Integer> secondPosFreqs = getTopFreqDigits(results,
		// DigitPosition.SECOND);
		// List<Integer> firstPosFreqs = getTopFreqDigits(results,
		// DigitPosition.FIRST);
		// List<Integer> allPosFreqs = getTopFreqDigits(results,
		// DigitPosition.NONE);

		listener.notifyStatus(listener.getMessage(R.string.message_generating_possible_combinations));
		List<String> predictedNumbers = new ArrayList<String>(100);
		for (int i = 0; i < results.prediction.pairingSets.size() - 1; i++) {
			String mainSet = results.prediction.pairingSets.get(i);
			for (int j = i + 1; j < results.prediction.pairingSets.size(); j++) {
				String number = mainSet + results.prediction.pairingSets.get(j);
				if (isSumWithinRange(results, number) && filteredByTopFreq(results, number, fourthPosFreqs)
				// && filteredByTopFreq(results, number, thirdPosFreqs)
				// && filteredByTopFreq(results, number, secondPosFreqs)
				// && filteredByTopFreq(results, number, firstPosFreqs)
				// && filteredByTopFreq(results, number, allPosFreqs)
				) {
					predictedNumbers.add(number);
				}
			}
		}

		for (String no : predictedNumbers) {
			if (!isPredictedNoExists(results, no)) {
				results.prediction.numbers.add(no);
			}
		}
	}

	private boolean isPredictedNoExists(final FrequencyAnalysisResults results, final String number) {
		for (String existingNo : results.prediction.numbers) {
			if (isMatchRandom(number, existingNo)) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private List<Integer> getTopFreqDigits(final FrequencyAnalysisResults results, final DigitPosition pos) {
		Map<Integer, Integer> digitPositions = results.getOrderedDigitFreq(pos);
		final Set<Entry<Integer, Integer>> mapValues = digitPositions.entrySet();
		final int mapLength = mapValues.size();
		final Entry<Integer, Integer>[] entries = new Entry[mapLength];
		mapValues.toArray(entries);
		List<Integer> topFreqDigits = new ArrayList<Integer>(5);
		topFreqDigits.add(entries[mapLength - 1].getKey());
		topFreqDigits.add(entries[mapLength - 2].getKey());
		topFreqDigits.add(entries[mapLength - 3].getKey());
		topFreqDigits.add(entries[mapLength - 4].getKey());
		topFreqDigits.add(entries[mapLength - 5].getKey());
		return topFreqDigits;
	}

	/**
	 * Get top pairings.
	 * 
	 * @param pairingSets
	 * @param mainDigit
	 * @param freq
	 */
	@SuppressWarnings("unchecked")
	private void getTopPairings(final FrequencyAnalysisResults results, final Integer mainDigit, final PairingFrequency freq) {
		Map<Integer, Integer> sortedMap = freq.toOrderedMap();
		final Set<Entry<Integer, Integer>> mapValues = sortedMap.entrySet();
		final int mapLength = mapValues.size();
		final Entry<Integer, Integer>[] entries = new Entry[mapLength];
		mapValues.toArray(entries);

		// Top 5 pairings
		addPairing(results.prediction, String.valueOf(mainDigit) + String.valueOf(entries[mapLength - 1].getKey()));
		addPairing(results.prediction, String.valueOf(mainDigit) + String.valueOf(entries[mapLength - 2].getKey()));
		addPairing(results.prediction, String.valueOf(mainDigit) + String.valueOf(entries[mapLength - 3].getKey()));
		addPairing(results.prediction, String.valueOf(mainDigit) + String.valueOf(entries[mapLength - 4].getKey()));
		addPairing(results.prediction, String.valueOf(mainDigit) + String.valueOf(entries[mapLength - 5].getKey()));
	}

	private void addPairing(final Prediction prediction, final String pair) {
		if (!existsPairing(prediction, pair)) {
			prediction.pairingSets.add(pair);
		}
	}

	private boolean existsPairing(final Prediction prediction, final String pairing) {
		for (String p : prediction.pairingSets) {
			if (isMatchRandom(p, pairing)) {
				return true;
			}
		}
		return false;
	}

	private boolean isSumWithinRange(final FrequencyAnalysisResults results, final String number) {
		int firstDigit = Integer.parseInt(number.substring(0, 1));
		int secondDigit = Integer.parseInt(number.substring(1, 2));
		int thirdDigit = Integer.parseInt(number.substring(2, 3));
		int fourthDigit = Integer.parseInt(number.substring(3, 4));
		int sum = firstDigit + secondDigit + thirdDigit + fourthDigit;

		return (sum >= results.sumofDigitsAnalysis.start && sum <= results.sumofDigitsAnalysis.end);
	}

	private boolean filteredByTopFreq(final FrequencyAnalysisResults results, final String number, final List<Integer> topFreqDigits) {
		int firstDigit = Integer.parseInt(number.substring(0, 1));
		int secondDigit = Integer.parseInt(number.substring(1, 2));
		int thirdDigit = Integer.parseInt(number.substring(2, 3));
		int fourthDigit = Integer.parseInt(number.substring(3, 4));

		if (topFreqDigits.contains(firstDigit) || topFreqDigits.contains(secondDigit) || topFreqDigits.contains(thirdDigit)
				|| topFreqDigits.contains(fourthDigit)) {
			return true;
		}
		/*
		 * List<Integer> numbers = new ArrayList<Integer>(4);
		 * numbers.add(firstDigit); numbers.add(secondDigit);
		 * numbers.add(thirdDigit); numbers.add(fourthDigit);
		 * 
		 * for (int i = 0, n = numbers.size(), f = fac(n); i < f; i++) {
		 * List<Integer> predictedList = permute(numbers, i);
		 * 
		 * return true; }
		 */
		return false;
	}

	// ////////////////////////End Prediction
	// ///////////////////////////////////////////

	// ////////////////////////Start Prediction
	// ///////////////////////////////////////////

	private void checkMatches(final FrequencyAnalysisResults results, final List<Result4D> unUsedResults) {
		listener.notifyStatus(listener.getMessage(R.string.message_check_probability));

		for (Result4D result : unUsedResults) {
			for (String no : results.prediction.numbers) {
				checkMatches(results, result, no);
			}
		}
	}

	private void checkMatches(final FrequencyAnalysisResults results, final Result4D result, final String predictedNo) {
		matchPrediction(results, result, result.firstPrize, predictedNo, PrizeType.FIRST_PRIZE);
		matchPrediction(results, result, result.secondPrize, predictedNo, PrizeType.SECOND_PRIZE);
		matchPrediction(results, result, result.thirdPrize, predictedNo, PrizeType.THIRD_PRIZE);

		for (String no : result.specialNumbers) {
			matchPrediction(results, result, no, predictedNo, PrizeType.SPECIAL);
		}

		for (String no : result.consolationNumbers) {
			matchPrediction(results, result, no, predictedNo, PrizeType.CONSOLATION);
		}

	}

	private void matchPrediction(final FrequencyAnalysisResults results, final Result4D result, final String number, final String predictedNo,
			final String prizeType) {
		if (isMatchExact(number, predictedNo)) {
			results.matches.matchedDraws.add(buildMatchedDraw(result, prizeType, number, true));
		} else if (isMatchRandom(result.firstPrize, predictedNo)) {
			results.matches.matchedDraws.add(buildMatchedDraw(result, prizeType, number, false));
		}
	}

	// ////////////////////////End Prediction
	// ///////////////////////////////////////////

	// ////////////////////////Start Position Frequency Analysis
	// ///////////////////////////////////////////

	/**
	 * Check number position frequency.
	 * 
	 * @param results
	 * @param result4D
	 */
	private void checkPositionFrequency(final FrequencyAnalysisResults results, final Result4D result4D) {

		// Top 3 prizes
		checkFrequencyByPosition(results, result4D.firstPrize);
		checkFrequencyByPosition(results, result4D.secondPrize);
		checkFrequencyByPosition(results, result4D.thirdPrize);

		// Special prizes
		for (String no : result4D.specialNumbers) {
			checkFrequencyByPosition(results, no);
		}

		// Consolation prizes
		for (String no : result4D.consolationNumbers) {
			checkFrequencyByPosition(results, no);
		}

	}

	private void checkFrequencyByPosition(final FrequencyAnalysisResults results, final String number) {
		int firstDigit = Integer.parseInt(number.substring(0, 1));
		int secondDigit = Integer.parseInt(number.substring(1, 2));
		int thirdDigit = Integer.parseInt(number.substring(2, 3));
		int fourthDigit = Integer.parseInt(number.substring(3, 4));

		checkFirstPosFrequency(results, firstDigit);
		checkSecondPosFrequency(results, secondDigit);
		checkThirdPosFrequency(results, thirdDigit);
		checkFourthPosFrequency(results, fourthDigit);
	}

	private void checkFirstPosFrequency(final FrequencyAnalysisResults results, final int digit) {
		if (digit == 0) {
			results.zeroNumberFreq.firstPositionFreq += 1;
			results.zeroNumberFreq.totalFreq += 1;
		} else if (digit == 1) {
			results.oneNumberFreq.firstPositionFreq += 1;
			results.oneNumberFreq.totalFreq += 1;
		} else if (digit == 2) {
			results.twoNumberFreq.firstPositionFreq += 1;
			results.twoNumberFreq.totalFreq += 1;
		} else if (digit == 3) {
			results.threeNumberFreq.firstPositionFreq += 1;
			results.threeNumberFreq.totalFreq += 1;
		} else if (digit == 4) {
			results.fourNumberFreq.firstPositionFreq += 1;
			results.fourNumberFreq.totalFreq += 1;
		} else if (digit == 5) {
			results.fiveNumberFreq.firstPositionFreq += 1;
			results.fiveNumberFreq.totalFreq += 1;
		} else if (digit == 6) {
			results.sixNumberFreq.firstPositionFreq += 1;
			results.sixNumberFreq.totalFreq += 1;
		} else if (digit == 7) {
			results.sevenNumberFreq.firstPositionFreq += 1;
			results.sevenNumberFreq.totalFreq += 1;
		} else if (digit == 8) {
			results.eightNumberFreq.firstPositionFreq += 1;
			results.eightNumberFreq.totalFreq += 1;
		} else if (digit == 9) {
			results.nineNumberFreq.firstPositionFreq += 1;
			results.nineNumberFreq.totalFreq += 1;
		}
	}

	private void checkSecondPosFrequency(final FrequencyAnalysisResults results, final int digit) {
		if (digit == 0) {
			results.zeroNumberFreq.secondPositionFreq += 1;
			results.zeroNumberFreq.totalFreq += 1;
		} else if (digit == 1) {
			results.oneNumberFreq.secondPositionFreq += 1;
			results.oneNumberFreq.totalFreq += 1;
		} else if (digit == 2) {
			results.twoNumberFreq.secondPositionFreq += 1;
			results.twoNumberFreq.totalFreq += 1;
		} else if (digit == 3) {
			results.threeNumberFreq.secondPositionFreq += 1;
			results.threeNumberFreq.totalFreq += 1;
		} else if (digit == 4) {
			results.fourNumberFreq.secondPositionFreq += 1;
			results.fourNumberFreq.totalFreq += 1;
		} else if (digit == 5) {
			results.fiveNumberFreq.secondPositionFreq += 1;
			results.fiveNumberFreq.totalFreq += 1;
		} else if (digit == 6) {
			results.sixNumberFreq.secondPositionFreq += 1;
			results.sixNumberFreq.totalFreq += 1;
		} else if (digit == 7) {
			results.sevenNumberFreq.secondPositionFreq += 1;
			results.sevenNumberFreq.totalFreq += 1;
		} else if (digit == 8) {
			results.eightNumberFreq.secondPositionFreq += 1;
			results.eightNumberFreq.totalFreq += 1;
		} else if (digit == 9) {
			results.nineNumberFreq.secondPositionFreq += 1;
			results.nineNumberFreq.totalFreq += 1;
		}
	}

	private void checkThirdPosFrequency(final FrequencyAnalysisResults results, final int digit) {
		if (digit == 0) {
			results.zeroNumberFreq.thirdPositionFreq += 1;
			results.zeroNumberFreq.totalFreq += 1;
		} else if (digit == 1) {
			results.oneNumberFreq.thirdPositionFreq += 1;
			results.oneNumberFreq.totalFreq += 1;
		} else if (digit == 2) {
			results.twoNumberFreq.thirdPositionFreq += 1;
			results.twoNumberFreq.totalFreq += 1;
		} else if (digit == 3) {
			results.threeNumberFreq.thirdPositionFreq += 1;
			results.threeNumberFreq.totalFreq += 1;
		} else if (digit == 4) {
			results.fourNumberFreq.thirdPositionFreq += 1;
			results.fourNumberFreq.totalFreq += 1;
		} else if (digit == 5) {
			results.fiveNumberFreq.thirdPositionFreq += 1;
			results.fiveNumberFreq.totalFreq += 1;
		} else if (digit == 6) {
			results.sixNumberFreq.thirdPositionFreq += 1;
			results.sixNumberFreq.totalFreq += 1;
		} else if (digit == 7) {
			results.sevenNumberFreq.thirdPositionFreq += 1;
			results.sevenNumberFreq.totalFreq += 1;
		} else if (digit == 8) {
			results.eightNumberFreq.thirdPositionFreq += 1;
			results.eightNumberFreq.totalFreq += 1;
		} else if (digit == 9) {
			results.nineNumberFreq.thirdPositionFreq += 1;
			results.nineNumberFreq.totalFreq += 1;
		}
	}

	private void checkFourthPosFrequency(final FrequencyAnalysisResults results, final int digit) {
		if (digit == 0) {
			results.zeroNumberFreq.fourthPositionFreq += 1;
			results.zeroNumberFreq.totalFreq += 1;
		} else if (digit == 1) {
			results.oneNumberFreq.fourthPositionFreq += 1;
			results.oneNumberFreq.totalFreq += 1;
		} else if (digit == 2) {
			results.twoNumberFreq.fourthPositionFreq += 1;
			results.twoNumberFreq.totalFreq += 1;
		} else if (digit == 3) {
			results.threeNumberFreq.fourthPositionFreq += 1;
			results.threeNumberFreq.totalFreq += 1;
		} else if (digit == 4) {
			results.fourNumberFreq.fourthPositionFreq += 1;
			results.fourNumberFreq.totalFreq += 1;
		} else if (digit == 5) {
			results.fiveNumberFreq.fourthPositionFreq += 1;
			results.fiveNumberFreq.totalFreq += 1;
		} else if (digit == 6) {
			results.sixNumberFreq.fourthPositionFreq += 1;
			results.sixNumberFreq.totalFreq += 1;
		} else if (digit == 7) {
			results.sevenNumberFreq.fourthPositionFreq += 1;
			results.sevenNumberFreq.totalFreq += 1;
		} else if (digit == 8) {
			results.eightNumberFreq.fourthPositionFreq += 1;
			results.eightNumberFreq.totalFreq += 1;
		} else if (digit == 9) {
			results.nineNumberFreq.fourthPositionFreq += 1;
			results.nineNumberFreq.totalFreq += 1;
		}
	}

	private void analyzeNumberFrequency(final FrequencyAnalysisResults results) {
		listener.notifyStatus(listener.getMessage(R.string.message_perform_frequency_analysis));

		// Freq positions
		determineFreqPosition(results.zeroNumberFreq, results.zeroNumberFreq.topNumberPositions, results.zeroNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.oneNumberFreq, results.oneNumberFreq.topNumberPositions, results.oneNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.twoNumberFreq, results.twoNumberFreq.topNumberPositions, results.twoNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.threeNumberFreq, results.threeNumberFreq.topNumberPositions, results.threeNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.fourNumberFreq, results.fourNumberFreq.topNumberPositions, results.fourNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.fiveNumberFreq, results.fiveNumberFreq.topNumberPositions, results.fiveNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.sixNumberFreq, results.sixNumberFreq.topNumberPositions, results.sixNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.sevenNumberFreq, results.sevenNumberFreq.topNumberPositions, results.sevenNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.eightNumberFreq, results.eightNumberFreq.topNumberPositions, results.eightNumberFreq.bottomNumberPositions);
		determineFreqPosition(results.nineNumberFreq, results.nineNumberFreq.topNumberPositions, results.nineNumberFreq.bottomNumberPositions);

		// Pairing
		determinePairingFreq(results.zeroPairingFreq, results.zeroPairingFreq.topPairedDigits, results.zeroPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.onePairingFreq, results.onePairingFreq.topPairedDigits, results.onePairingFreq.bottomPairedDigits);
		determinePairingFreq(results.twoPairingFreq, results.twoPairingFreq.topPairedDigits, results.twoPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.threePairingFreq, results.threePairingFreq.topPairedDigits, results.threePairingFreq.bottomPairedDigits);
		determinePairingFreq(results.fourPairingFreq, results.fourPairingFreq.topPairedDigits, results.fourPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.fivePairingFreq, results.fivePairingFreq.topPairedDigits, results.fivePairingFreq.bottomPairedDigits);
		determinePairingFreq(results.sixPairingFreq, results.sixPairingFreq.topPairedDigits, results.sixPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.sevenPairingFreq, results.sevenPairingFreq.topPairedDigits, results.sevenPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.eightPairingFreq, results.eightPairingFreq.topPairedDigits, results.eightPairingFreq.bottomPairedDigits);
		determinePairingFreq(results.ninePairingFreq, results.ninePairingFreq.topPairedDigits, results.ninePairingFreq.bottomPairedDigits);

	}

	private void determinePairingFreq(final PairingFrequency freq, final List<Integer> topPositions, final List<Integer> bottomPositions) {

		Map<Integer, Integer> digits = new HashMap<Integer, Integer>(10);
		digits.put(0, freq.zeroFreq);
		digits.put(1, freq.oneFreq);
		digits.put(2, freq.twoFreq);
		digits.put(3, freq.threeFreq);
		digits.put(4, freq.fourFreq);
		digits.put(5, freq.fiveFreq);
		digits.put(6, freq.sixFreq);
		digits.put(7, freq.sevenFreq);
		digits.put(8, freq.eightFreq);
		digits.put(9, freq.nineFreq);

		Map<Integer, Integer> sortedDigits = null;
		sortedDigits = AnalysisHelper.sortByValueDesc(digits);
		derivePairingPosition(sortedDigits, topPositions);
		sortedDigits = AnalysisHelper.sortByValue(digits);
		derivePairingPosition(sortedDigits, bottomPositions);
		Integer[] valueList = new Integer[1];
		valueList = sortedDigits.keySet().toArray(valueList);
		freq.median = valueList[valueList.length / 2];
	}

	private void derivePairingPosition(final Map<Integer, Integer> sortedDigits, final List<Integer> positions) {
		for (Integer p1 : sortedDigits.keySet()) {
			if (positions.size() == 0) {
				positions.add(p1);
			}
			if (!positions.contains(p1)) {
				boolean toAdd = false;
				for (Integer p2 : positions) {
					if (sortedDigits.get(p2) == sortedDigits.get(p1)) {
						toAdd = true;
						break;
					}
				}
				if (toAdd)
					positions.add(p1);
			}
		}
	}

	private void determineFreqPosition(final NumberFrequency freq, final List<DigitPosition> topPositions, final List<DigitPosition> bottomPosition) {

		Map<DigitPosition, Integer> digits = new HashMap<DigitPosition, Integer>(4);
		digits.put(DigitPosition.FIRST, freq.firstPositionFreq);
		digits.put(DigitPosition.SECOND, freq.secondPositionFreq);
		digits.put(DigitPosition.THIRD, freq.thirdPositionFreq);
		digits.put(DigitPosition.FOURTH, freq.fourthPositionFreq);

		Map<DigitPosition, Integer> sortedDigits = null;
		sortedDigits = AnalysisHelper.sortByValueDesc(digits);
		deriveFreqPosition(sortedDigits, topPositions);
		sortedDigits = AnalysisHelper.sortByValue(digits);
		deriveFreqPosition(sortedDigits, bottomPosition);

	}

	private void deriveFreqPosition(final Map<DigitPosition, Integer> sortedDigits, final List<DigitPosition> positions) {
		for (DigitPosition p1 : sortedDigits.keySet()) {
			if (positions.size() == 0) {
				positions.add(p1);
			}
			if (!positions.contains(p1)) {
				boolean toAdd = false;
				for (DigitPosition p2 : positions) {
					if (sortedDigits.get(p2) == sortedDigits.get(p1)) {
						toAdd = true;
						break;
					}
				}
				if (toAdd)
					positions.add(p1);
			}
		}
	}

	// //////////////////////// End Position Frequency Analysis
	// ///////////////////////////////////////////

	// //////////////////////// Start Pairing Frequency Analysis
	// ///////////////////////////////////////////

	private void checkPairingFrequency(final FrequencyAnalysisResults results, final Result4D result4D) {

		// Top 3 prizes
		checkNumberPairingFreq(results, result4D.firstPrize);
		checkNumberPairingFreq(results, result4D.secondPrize);
		checkNumberPairingFreq(results, result4D.thirdPrize);

		// Special prizes
		for (String no : result4D.specialNumbers) {
			checkNumberPairingFreq(results, no);
		}

		// Consolation prizes
		for (String no : result4D.consolationNumbers) {
			checkNumberPairingFreq(results, no);
		}
	}

	public void checkNumberPairingFreq(final FrequencyAnalysisResults results, final String number) {
		int firstDigit = Integer.parseInt(number.substring(0, 1));
		int secondDigit = Integer.parseInt(number.substring(1, 2));
		int thirdDigit = Integer.parseInt(number.substring(2, 3));
		int fourthDigit = Integer.parseInt(number.substring(3, 4));

		List<Integer> digits = new ArrayList<Integer>(4);
		digits.add(firstDigit);
		digits.add(secondDigit);
		digits.add(thirdDigit);
		digits.add(fourthDigit);

		List<Integer> duplicates = getDuplicate(digits);
		List<Integer> nonDuplicates = getNonDuplicate(digits);

		if (nonDuplicates.size() > 1) {
			int repeat = nonDuplicates.size();
			for (int i = 0; i < repeat; i++) {
				List<Integer> list = new ArrayList<Integer>(repeat);
				Integer mainDigit = 0;
				for (int j = 0; j < repeat; j++) {
					if (j != i) {
						list.add(nonDuplicates.get(j));
					} else {
						mainDigit = nonDuplicates.get(j);
					}
				}
				checkAllPossiblePairing(results, mainDigit, list);
			}
			/*
			 * for (int i = 0, n = nonDuplicates.size(), f = fac(n); i < f; i++)
			 * { List<Integer> i1 = permute(nonDuplicates, i); Integer[] d = new
			 * Integer[i1.size()]; checkAllPossiblePairing(results,
			 * i1.toArray(d)); }
			 */

		}

		if (duplicates.size() >= 1) {
			for (int d1 : duplicates) {
				for (int d2 : nonDuplicates) {
					checkAllPossiblePairing(results, d1, d2);
				}
			}
		}
	}

	public void checkAllPossiblePairing(final FrequencyAnalysisResults results, final Integer... digits) {
		PairingFrequency freq = results.lookupPairingFreq.get(digits[0]);
		for (int i = 1; i < digits.length; i++) {
			checkPairingFreqDigit(results, freq, digits[i]);
		}
	}

	public void checkAllPossiblePairing(final FrequencyAnalysisResults results, final Integer mainDigit, final List<Integer> digits) {
		PairingFrequency freq = results.lookupPairingFreq.get(mainDigit);
		for (Integer digit : digits) {
			checkPairingFreqDigit(results, freq, digit);
		}
	}

	/*
	 * public void checkAllPossiblePairing(final FrequencyAnalysisResults
	 * results, final int mainDigit, final int digit1, final int digit2, final
	 * int digit3) { PairingFrequency freq =
	 * results.lookupPairingFreq.get(mainDigit); checkPairingFreqDigit(results,
	 * freq, digit1); checkPairingFreqDigit(results, freq, digit2);
	 * checkPairingFreqDigit(results, freq, digit3); }
	 */

	public void checkPairingFreqDigit(final FrequencyAnalysisResults results, final PairingFrequency pairingFreq, final int digit) {
		if (digit == 0) {
			pairingFreq.zeroFreq += 1;
		} else if (digit == 1) {
			pairingFreq.oneFreq += 1;
		} else if (digit == 2) {
			pairingFreq.twoFreq += 1;
		} else if (digit == 3) {
			pairingFreq.threeFreq += 1;
		} else if (digit == 4) {
			pairingFreq.fourFreq += 1;
		} else if (digit == 5) {
			pairingFreq.fiveFreq += 1;
		} else if (digit == 6) {
			pairingFreq.sixFreq += 1;
		} else if (digit == 7) {
			pairingFreq.sevenFreq += 1;
		} else if (digit == 8) {
			pairingFreq.eightFreq += 1;
		} else if (digit == 9) {
			pairingFreq.nineFreq += 1;
		}
	}

	public <T> List<T> getDuplicate(final Collection<T> list) {

		final List<T> duplicatedObjects = new ArrayList<T>();
		Set<T> set = new LinkedHashSet<T>() {

			private static final long serialVersionUID = -4461088687108011257L;

			@Override
			public boolean add(T e) {
				if (contains(e)) {
					duplicatedObjects.add(e);
				}
				return super.add(e);
			}
		};
		for (T t : list) {
			set.add(t);
		}
		return duplicatedObjects;
	}

	public <T> List<T> getNonDuplicate(final Collection<T> list) {

		final List<T> nonDuplicatedObjects = new ArrayList<T>();
		Set<T> set = new LinkedHashSet<T>() {

			private static final long serialVersionUID = -4461088687108011257L;

			@Override
			public boolean add(T e) {
				if (!contains(e)) {
					nonDuplicatedObjects.add(e);
				}
				return super.add(e);
			}
		};
		for (T t : list) {
			set.add(t);
		}
		return nonDuplicatedObjects;
	}

	public <T> boolean hasDuplicate(final Collection<T> list) {
		if (getDuplicate(list).isEmpty())
			return false;
		return true;
	}

	public int fac(int n) {
		int f = 1;
		for (; n > 0; f *= n--)
			;
		return f;
	}

	public <T> List<T> permute(List<T> list, int r) {

		int n = list.size();
		int f = fac(n);
		List<T> perm = new ArrayList<T>();

		list = new ArrayList<T>(list);
		for (list = new ArrayList<T>(list); n > 0; n--, r %= f) {

			f /= n;
			perm.add(list.remove(r / f));
		}
		return perm;
	}

	// ////////////////////////End Pairing Frequency Analysis
	// ///////////////////////////////////////////

	// ////////////////////////Start Sum of Digits Analysis
	// ///////////////////////////////////////////

	private void checkSumofDigits(final FrequencyAnalysisResults results, final Result4D result4D) {
		SumofDigits firstPrize = results.new SumofDigits(result4D.firstPrize, result4D.drawDate, result4D.drawDay, PrizeType.FIRST_PRIZE);
		results.sumofDigitsAnalysis.addSumofDigits(firstPrize);

		SumofDigits secondPrize = results.new SumofDigits(result4D.secondPrize, result4D.drawDate, result4D.drawDay, PrizeType.SECOND_PRIZE);
		results.sumofDigitsAnalysis.addSumofDigits(secondPrize);

		SumofDigits thirdPrize = results.new SumofDigits(result4D.thirdPrize, result4D.drawDate, result4D.drawDay, PrizeType.THIRD_PRIZE);
		results.sumofDigitsAnalysis.addSumofDigits(thirdPrize);

		// Special prizes
		for (String no : result4D.specialNumbers) {
			SumofDigits special = results.new SumofDigits(no, result4D.drawDate, result4D.drawDay, PrizeType.SPECIAL);
			results.sumofDigitsAnalysis.addSumofDigits(special);
		}

		// Consolation prizes
		for (String no : result4D.consolationNumbers) {
			SumofDigits consolation = results.new SumofDigits(no, result4D.drawDate, result4D.drawDay, PrizeType.CONSOLATION);
			results.sumofDigitsAnalysis.addSumofDigits(consolation);
		}
	}

	private void analyzeSumofDigits(final FrequencyAnalysisResults results) {
		listener.notifyStatus(listener.getMessage(R.string.message_perform_sum_of_digits_analysis));
		results.sumofDigitsAnalysis.analyze();
	}

	// ////////////////////////End Sum of Digits Analysis
	// ///////////////////////////////////////////

}
