package com.mylotto.analysis;

public interface IAnalysisListener {
	String getMessage(int resId);
	void notifyStatus(String status);
}
