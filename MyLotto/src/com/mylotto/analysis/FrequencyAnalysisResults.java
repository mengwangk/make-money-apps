package com.mylotto.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Frequency analysis results
 * 
 * @author MEKOH
 *
 */
public final class FrequencyAnalysisResults {

	public Date from;
	public Date until;
	public int totalDraws = 0;
	public final FrequencyAnalysisQuery query;

	public final NumberFrequency zeroNumberFreq = new NumberFrequency(0);
	public final NumberFrequency oneNumberFreq = new NumberFrequency(1);
	public final NumberFrequency twoNumberFreq = new NumberFrequency(2);
	public final NumberFrequency threeNumberFreq = new NumberFrequency(3);
	public final NumberFrequency fourNumberFreq = new NumberFrequency(4);
	public final NumberFrequency fiveNumberFreq = new NumberFrequency(5);
	public final NumberFrequency sixNumberFreq = new NumberFrequency(6);
	public final NumberFrequency sevenNumberFreq = new NumberFrequency(7);
	public final NumberFrequency eightNumberFreq = new NumberFrequency(8);
	public final NumberFrequency nineNumberFreq = new NumberFrequency(9);

	public final PairingFrequency zeroPairingFreq = new PairingFrequency(0);
	public final PairingFrequency onePairingFreq = new PairingFrequency(1);
	public final PairingFrequency twoPairingFreq = new PairingFrequency(2);
	public final PairingFrequency threePairingFreq = new PairingFrequency(3);
	public final PairingFrequency fourPairingFreq = new PairingFrequency(4);
	public final PairingFrequency fivePairingFreq = new PairingFrequency(5);
	public final PairingFrequency sixPairingFreq = new PairingFrequency(6);
	public final PairingFrequency sevenPairingFreq = new PairingFrequency(7);
	public final PairingFrequency eightPairingFreq = new PairingFrequency(8);
	public final PairingFrequency ninePairingFreq = new PairingFrequency(9);

	public final Map<Integer, PairingFrequency> lookupPairingFreq = new HashMap<Integer, PairingFrequency>() {
		
		private static final long serialVersionUID = 8598688120808855983L;

		{
			put(0, zeroPairingFreq);
			put(1, onePairingFreq);
			put(2, twoPairingFreq);
			put(3, threePairingFreq);
			put(4, fourPairingFreq);
			put(5, fivePairingFreq);
			put(6, sixPairingFreq);
			put(7, sevenPairingFreq);
			put(8, eightPairingFreq);
			put(9, ninePairingFreq);
		}
	};

	public final SumofDigitsAnalysis sumofDigitsAnalysis = new SumofDigitsAnalysis();
	public final Prediction prediction = new Prediction();
	public final Matches matches = new Matches();

	/**
	 * Constructor.
	 * 
	 * @param query
	 */
	public FrequencyAnalysisResults(final FrequencyAnalysisQuery query) {
		this.query = query;
	}
	
	
	public Map<Integer, Integer> getOrderedDigitFreq(DigitPosition position){
		Map<Integer, Integer> digits = new HashMap<Integer, Integer>(10);
		if (position == DigitPosition.FIRST){
			digits.put(0, zeroNumberFreq.firstPositionFreq);
			digits.put(1, oneNumberFreq.firstPositionFreq);
			digits.put(2, twoNumberFreq.firstPositionFreq);
			digits.put(3, threeNumberFreq.firstPositionFreq);
			digits.put(4, fourNumberFreq.firstPositionFreq);
			digits.put(5, fiveNumberFreq.firstPositionFreq);
			digits.put(6, sixNumberFreq.firstPositionFreq);
			digits.put(7, sevenNumberFreq.firstPositionFreq);
			digits.put(8, eightNumberFreq.firstPositionFreq);
			digits.put(9, nineNumberFreq.firstPositionFreq);
		} else if (position == DigitPosition.SECOND){
			digits.put(0, zeroNumberFreq.secondPositionFreq);
			digits.put(1, oneNumberFreq.secondPositionFreq);
			digits.put(2, twoNumberFreq.secondPositionFreq);
			digits.put(3, threeNumberFreq.secondPositionFreq);
			digits.put(4, fourNumberFreq.secondPositionFreq);
			digits.put(5, fiveNumberFreq.secondPositionFreq);
			digits.put(6, sixNumberFreq.secondPositionFreq);
			digits.put(7, sevenNumberFreq.secondPositionFreq);
			digits.put(8, eightNumberFreq.secondPositionFreq);
			digits.put(9, nineNumberFreq.secondPositionFreq);
		} else if (position == DigitPosition.THIRD){
			digits.put(0, zeroNumberFreq.thirdPositionFreq);
			digits.put(1, oneNumberFreq.thirdPositionFreq);
			digits.put(2, twoNumberFreq.thirdPositionFreq);
			digits.put(3, threeNumberFreq.thirdPositionFreq);
			digits.put(4, fourNumberFreq.thirdPositionFreq);
			digits.put(5, fiveNumberFreq.thirdPositionFreq);
			digits.put(6, sixNumberFreq.thirdPositionFreq);
			digits.put(7, sevenNumberFreq.thirdPositionFreq);
			digits.put(8, eightNumberFreq.thirdPositionFreq);
			digits.put(9, nineNumberFreq.thirdPositionFreq);
		} else if (position == DigitPosition.FOURTH){
			digits.put(0, zeroNumberFreq.fourthPositionFreq);
			digits.put(1, oneNumberFreq.fourthPositionFreq);
			digits.put(2, twoNumberFreq.fourthPositionFreq);
			digits.put(3, threeNumberFreq.fourthPositionFreq);
			digits.put(4, fourNumberFreq.fourthPositionFreq);
			digits.put(5, fiveNumberFreq.fourthPositionFreq);
			digits.put(6, sixNumberFreq.fourthPositionFreq);
			digits.put(7, sevenNumberFreq.fourthPositionFreq);
			digits.put(8, eightNumberFreq.fourthPositionFreq);
			digits.put(9, nineNumberFreq.fourthPositionFreq);
		} else {
			digits.put(0, zeroNumberFreq.totalFreq);
			digits.put(1, oneNumberFreq.totalFreq);
			digits.put(2, twoNumberFreq.totalFreq);
			digits.put(3, threeNumberFreq.totalFreq);
			digits.put(4, fourNumberFreq.totalFreq);
			digits.put(5, fiveNumberFreq.totalFreq);
			digits.put(6, sixNumberFreq.totalFreq);
			digits.put(7, sevenNumberFreq.totalFreq);
			digits.put(8, eightNumberFreq.totalFreq);
			digits.put(9, nineNumberFreq.totalFreq);
		}
		return AnalysisHelper.sortByValue(digits);
	}

	/**
	 * Number frequency class.
	 * 
	 * @author MEKOH
	 *
	 */
	public final class NumberFrequency {
		public final int digit;
		public int totalFreq = 0;
		public int firstPositionFreq = 0;
		public int secondPositionFreq = 0;
		public int thirdPositionFreq = 0;
		public int fourthPositionFreq = 0;
		public List<DigitPosition> topNumberPositions;
		public List<DigitPosition> bottomNumberPositions;

		/**
		 * Constructor.
		 * 
		 * @param digit
		 */
		public NumberFrequency(final int digit) {
			this.digit = digit;
			this.topNumberPositions = new ArrayList<DigitPosition>(1);
			this.bottomNumberPositions = new ArrayList<DigitPosition>(1);
		}
	}

	/**
	 * Pairing frequency class.
	 * 
	 * @author MEKOH
	 *
	 */
	public final class PairingFrequency {
		public final int digit;

		public int zeroFreq = 0;
		public int oneFreq = 0;
		public int twoFreq = 0;
		public int threeFreq = 0;
		public int fourFreq = 0;
		public int fiveFreq = 0;
		public int sixFreq = 0;
		public int sevenFreq = 0;
		public int eightFreq = 0;
		public int nineFreq = 0;

		public List<Integer> topPairedDigits;
		public List<Integer> bottomPairedDigits;
		public Integer median;
		
		/**
		 * Constructor.
		 * 
		 * @param digit
		 */
		public PairingFrequency(final int digit) {
			this.digit = digit;
			this.topPairedDigits = new ArrayList<Integer>(1);
			this.bottomPairedDigits = new ArrayList<Integer>(1);
			this.median = 0;
		}
		
		public Map<Integer, Integer> toOrderedMap() {
			Map<Integer, Integer> values = new HashMap<Integer, Integer>(10);
			values.put(0, zeroFreq);
			values.put(1, oneFreq);
			values.put(2, twoFreq);
			values.put(3, threeFreq);
			values.put(4, fourFreq);
			values.put(5, fiveFreq);
			values.put(6, sixFreq);
			values.put(7, sevenFreq);
			values.put(8, eightFreq);
			values.put(9, nineFreq);
			
			return AnalysisHelper.sortByValue(values);
		}
	}

	/**
	 * Sum of digits analysis.
	 * 
	 * @author MEKOH
	 *
	 */
	public final class SumofDigitsAnalysis {
		private final List<SumofDigits> allSumofDigits = new ArrayList<SumofDigits>(30);

		public double percentage = 80;
		public int start = 0; // e.g. 0000
		public int end = 36; // e.g. 9999
		public final SortedMap<Integer, Integer> sumofDigitsFrequency = new TreeMap<Integer, Integer>();

		public void addSumofDigits(final SumofDigits s) {
			this.allSumofDigits.add(s);

			// Calculate the frequency
			if (sumofDigitsFrequency.containsKey(s.sum)) {
				Integer freq = sumofDigitsFrequency.get(s.sum);
				sumofDigitsFrequency.put(s.sum, ++freq);
			} else {
				sumofDigitsFrequency.put(s.sum, 1);
			}

		}

		/**
		 * Perform analysis now
		 */
		public void analyze() {
			int totalPercentageNo = (int) (percentage / 100 * allSumofDigits.size());
			int middle = sumofDigitsFrequency.size() / 2;
			int pos1 = middle;
			int pos2 = middle;
			if (sumofDigitsFrequency.size() % 2 != 1) {
				pos1 = middle - 1;
				pos2 = middle;
			}

			int sum = 0;
			Integer[] keys = new Integer[sumofDigitsFrequency.keySet().size()];
			keys = sumofDigitsFrequency.keySet().toArray(keys);
			while (true) {
				sum += sumofDigitsFrequency.get(keys[pos1]);
				if (sum >= totalPercentageNo) {
					start = keys[pos1];
					end = keys[pos2];
					break;
				} else {
					pos1--;
				}
				sum += sumofDigitsFrequency.get(keys[pos2]);
				if (sum >= totalPercentageNo) {
					start = keys[pos1];
					end = keys[pos2];
					break;
				} else {
					pos2++;
				}
			}

		}
	}

	/**
	 * Sum of digit
	 * 
	 * @author MEKOH
	 *
	 */
	public final class SumofDigits {

		public final String number;
		public final Date drawDate;
		public final String drawDay;
		public final String prizeType;
		public final int sum;

		/**
		 * Constructor
		 * 
		 * @param number
		 * @param drawDate
		 * @param drawDay
		 * @param prizeType
		 */
		public SumofDigits(final String number, final Date drawDate, final String drawDay, final String prizeType) {
			this.number = number;
			this.drawDate = drawDate;
			this.drawDay = drawDay;
			this.prizeType = prizeType;

			int firstDigit = Integer.parseInt(number.substring(0, 1));
			int secondDigit = Integer.parseInt(number.substring(1, 2));
			int thirdDigit = Integer.parseInt(number.substring(2, 3));
			int fourthDigit = Integer.parseInt(number.substring(3, 4));
			sum = firstDigit + secondDigit + thirdDigit + fourthDigit;
		}
	}

	
	/**
	 * Prediction class.
	 * 
	 * @author MEKOH
	 *
	 */
	public final class Prediction {
		public final List<String> pairingSets = new ArrayList<String>(10);
		public final List<String> numbers = new ArrayList<String>(10);
	}
	
	/**
	 * Matches class.
	 * 
	 * @author MEKOH
	 *
	 */
	public final class Matches {
		public final List<MatchedDraw> matchedDraws = new ArrayList<MatchedDraw>(10);
	}
	
	/**
	 * Digit position
	 * 
	 * @author MEKOH
	 *
	 */
	public enum DigitPosition {
		NONE,
		FIRST,
		SECOND,
		THIRD,
		FOURTH
	}

}
