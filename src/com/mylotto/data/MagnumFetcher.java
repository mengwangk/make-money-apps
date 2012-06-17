package com.mylotto.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;

import com.mylotto.data.FetchResults.FetchStatus;
import com.mylotto.helper.DateUtils;
import com.mylotto.helper.DbHelper;
import com.mylotto.helper.StringUtils;

/**
 * Fetch and store MAGNUM results.
 * 
 * Start from the START_PAGE and fetch all the draw nos for the current month
 * Check for each draw no if the results are in database, if not then insert the draw results
 * Check maximum number of draw results to be stored in database settings for this lotto
 * If not maximum yet, continue to back date to previous month to retrieve the results
 * After loading for previous maximum, purge the old draw results
 * 
 * Note: When checking the START_PAGE, if the draw no is 999/99, skip it.
 * 
 * @author MEKOH
 *
 */
public class MagnumFetcher extends BaseFetcher {

	public static final String[] MONTHS = new String[] { "Jan", "Feb", "Mar",
			"Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	private static final String MONTH_PARAM = "DrawMonth";

	private static final String YEAR_PARAM = "DrawYear";

	private static final String TABLE_NAME = "magnum";

	private static final String STATISTICS_PAGE = "http://www.magnumit.com/magnum4d/MDresults_statistics.asp";

	private static final String RESULTS_QUERY = "http://www.magnumit.com/magnum4d/MDresults&statistics_popup.asp?drawid=%s";

	private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])[\\./](0?[1-9]|1[012])[\\./]((19|20)\\d\\d)";

	private static final String DRAW_NO_PATTERN = "\\d\\d\\d/\\d\\d";

	private static final String INVALID_DRAW_NO = "999/99";

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param context
	 * @param prefs
	 */
	public MagnumFetcher(String name, Context context, Prefs prefs) {
		super(name, context, prefs);
	}

	public void run() {

		// Also check the last successful loading, if on the same day, then just return
		if (DateUtils.isToday(lastSuccessfulDrawDate)) return;

		synchronizeResults();

	}

	public void synchronizeResults() {
		DbHelper dbHelper = new DbHelper(context);
		try {

			// Number of draws to keep
			int maxNoOfDraw = Integer.parseInt(prefs.getNoOfDraw());

			// Retrieve results for current month and year
			Calendar startDt = Calendar.getInstance();
			int month = startDt.get(Calendar.MONTH);
			int year = startDt.get(Calendar.YEAR);

			List<Date> allDrawDates = null;
			boolean isCompleted = false;
			int noOfDraw = 0;
			boolean isSynchSuccessful = false;
			while (!isCompleted) {
				List<String> drawNumbers = retrieveDrawNumbers(month, year);
				if (drawNumbers.size() > 0) {
					//noOfDraw += drawNumbers.size();
					//for (String drawNo : drawNumbers) {
					for (int i = drawNumbers.size() - 1; i >= 0; i--) {
						String drawNo = drawNumbers.get(i);
						
						// For each draw numbers, check if already exist in database, if not then retrieve the results
						Log.d(CLASS_TAG, "draw no:" + drawNo);

						Result4D result = dbHelper.getResult4DByDrawNo(
								TABLE_NAME, drawNo);
						if ("".equals(result.drawNo)) {
							// Result does not exist, proceed to download
							if (!downloadResults(drawNo, dbHelper)) {
								isCompleted = true;
								break; // Stop if cannot download
							}
						}
						noOfDraw++;
						if (noOfDraw >= maxNoOfDraw)
							break;
					}
				}

				if (!isCompleted) {
					if (noOfDraw >= maxNoOfDraw) {
						isCompleted = true;
						isSynchSuccessful = true;

						// Check and remove old draw results 
						allDrawDates = dbHelper.getAllDrawDates(TABLE_NAME);
						if (allDrawDates.size() > maxNoOfDraw) {
							for (int i = maxNoOfDraw; i < allDrawDates.size(); i++) {
								dbHelper.deleteLottoByDrawDate(TABLE_NAME,
										allDrawDates.get(i));
							}
						}
					} else {
						startDt = DateUtils.getPreviousMonth(startDt);
						month = startDt.get(Calendar.MONTH);
						year = startDt.get(Calendar.YEAR);
					}
				}
			}

			if (isSynchSuccessful) {
				// Set the last successful draw date
				allDrawDates = dbHelper.getAllDrawDates(TABLE_NAME);
				lastSuccessfulDrawDate = allDrawDates.get(0);
			}

			results.status = FetchStatus.SUCCESSFUL;
			dbHelper.cleanUp();
			dbHelper = null;

		} catch (Exception ex) {
			results.status = FetchStatus.UNKNOWN_ERROR;
			results.errorMsg = ex.getMessage();

		} finally {
			if (dbHelper != null) {
				dbHelper.cleanUp();
				dbHelper = null;
			}
		}
	}

	/**
	 * Retrieve a list of draw numbers and process them if necessary
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	private List<String> retrieveDrawNumbers(final int month, final int year) {
		List<String> drawNumbers = new ArrayList<String>(5);
		Map<String, String> params = new HashMap<String, String>(2);
		params.put(MONTH_PARAM, "" + MONTHS[month]);
		params.put(YEAR_PARAM, "" + year);
		FetchResults results = performPost(STATISTICS_PAGE, params);
		if (results.status == FetchStatus.SUCCESSFUL) {
			Pattern patt = Pattern.compile(DRAW_NO_PATTERN);
			Matcher m = patt.matcher(results.content);
			while (m.find()) {
				String drawNo = m.group();
				if (!INVALID_DRAW_NO.equals(drawNo)) {
					drawNumbers.add(drawNo);
				}
			}
		}
		return drawNumbers;
	}

	/**
	 * Download the 4D results.
	 * 
	 * @param drawNo
	 * @return
	 */
	private boolean downloadResults(final String drawNo, final DbHelper dbHelper) {
		String url = String.format(RESULTS_QUERY, drawNo);
		FetchResults result = performGet(url);
		if (result.status != FetchStatus.SUCCESSFUL)
			return false;
		return processContent(result.content, dbHelper);
	}

	/**
	 * Parse and load the lotto results into database.
	 * 
	 * @param pageContent Page content
	 * @param dbHelper Database helper
	 * @return Processing status
	 */
	private boolean processContent(final String pageContent,
			final DbHelper dbHelper) {
		String[] lines = pageContent.split("\\r?\\n");
		Result4D result4D = new Result4D();
		int lineNo = -1;
		for (String line : lines) {
			line = line.trim().toLowerCase();
			lineNo++;

			// Check for draw no
			if (line.matches("(?i).*draw no.*")) {
				String[] cols = line.split(":");
				if (cols.length > 0) {
					result4D.drawNo = cols[1].trim();
					continue;
				}
			}

			if (result4D.drawDate == null) {
				// Check for date
				result4D.drawDate = DateUtils.getDate(line, DATE_PATTERN);
				if (result4D.drawDate != null) {
					Log.d(CLASS_TAG, "Draw date: [" + result4D.drawDate + "]");
					continue;
				}
			}

			if (line.contains("1st prize")) {
				result4D.firstPrize = lines[lineNo + 1].trim();
				Log.d(CLASS_TAG, "First prize: [" + result4D.firstPrize + "]");
				continue;
			}

			if (line.contains("2nd prize")) {
				result4D.secondPrize = lines[lineNo + 1].trim();
				Log.d(CLASS_TAG, "Second prize: [" + result4D.secondPrize + "]");
				continue;
			}

			if (line.contains("3rd prize")) {
				result4D.thirdPrize = lines[lineNo + 1].trim();
				Log.d(CLASS_TAG, "Third prize: [" + result4D.thirdPrize + "]");
				continue;
			}

			if (line.contains("consolation")) {
				int start = lineNo + 1;

				result4D.specialNumbers.add(lines[start++].trim());
				result4D.specialNumbers.add(lines[start++].trim());

				result4D.consolationNumbers.add(lines[start++].trim());
				result4D.consolationNumbers.add(lines[start++].trim());

				result4D.specialNumbers.add(lines[start++].trim());
				result4D.specialNumbers.add(lines[start++].trim());

				result4D.consolationNumbers.add(lines[start++].trim());
				result4D.consolationNumbers.add(lines[start++].trim());

				result4D.specialNumbers.add(lines[start++].trim());
				result4D.specialNumbers.add(lines[start++].trim());

				result4D.consolationNumbers.add(lines[start++].trim());
				result4D.consolationNumbers.add(lines[start++].trim());

				result4D.specialNumbers.add(lines[start++].trim());
				result4D.specialNumbers.add(lines[start++].trim());

				result4D.consolationNumbers.add(lines[start++].trim());
				result4D.consolationNumbers.add(lines[start++].trim());

				result4D.specialNumbers.add(lines[start++].trim());
				result4D.specialNumbers.add(lines[start++].trim());

				result4D.consolationNumbers.add(lines[start++].trim());
				result4D.consolationNumbers.add(lines[start++].trim());

				break;
			}

		}

		if (StringUtils.isNullorEmpty(result4D.drawNo)) {
			Log.e(CLASS_TAG, "Missing draw no");
			return false;
		}

		if (result4D.drawDate == null) {
			Log.e(CLASS_TAG, "Missing draw date");
			return false;
		}

		if (StringUtils.isNullorEmpty(result4D.firstPrize)) {
			Log.e(CLASS_TAG, "Missing first prize");
			return false;
		}

		if (StringUtils.isNullorEmpty(result4D.secondPrize)) {
			Log.e(CLASS_TAG, "Missing second prize");
			return false;
		}

		if (StringUtils.isNullorEmpty(result4D.thirdPrize)) {
			Log.e(CLASS_TAG, "Missing third prize");
			return false;
		}

		if (result4D.specialNumbers.size() != 10) {
			Log.e(CLASS_TAG, "Only has " + result4D.specialNumbers.size()
					+ " special numbers");
			return false;
		}

		if (result4D.consolationNumbers.size() != 10) {
			Log.e(CLASS_TAG, "Only has " + result4D.specialNumbers.size()
					+ " consolation numbers");
			return false;
		}

		// Start to load to database

		// Check if the data already exist
		Result4D existingResult = dbHelper.getResult4DByDrawNo(TABLE_NAME,
				result4D.drawNo);
		if (!StringUtils.isNullorEmpty(existingResult.firstPrize)) {
			// Already exist, no need to insert
			Log.i(CLASS_TAG, "Results for " + result4D.drawNo
					+ " already exists. Skip");
		} else {
			dbHelper.insert4D(TABLE_NAME, result4D);
		}
		return true;
	}

}
