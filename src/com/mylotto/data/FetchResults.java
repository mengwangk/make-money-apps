package com.mylotto.data;

import com.mylotto.helper.StringUtils;

/**
 * Fetch results.
 * 
 * @author MEKOH
 *
 */
public final class FetchResults {
	
	public enum FetchStatus {
		SUCCESSFUL, CONNECTION_NOT_AVAILABLE, PARSING_ERROR, INVALID_URI, UNABLE_TO_READ, DATABASE_ERROR, UNKNOWN_ERROR,
	}

	public enum FetchProgress {
		NOT_STARTED, IN_PROGRESS, COMPLETED
	};
	
	public FetchStatus status = FetchStatus.UNKNOWN_ERROR;
	public String content = StringUtils.EMPTY;
	public String errorMsg = StringUtils.EMPTY;
}
