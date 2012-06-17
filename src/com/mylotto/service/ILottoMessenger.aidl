package com.mylotto.service;

import com.mylotto.data.Result4D;
import com.mylotto.data.SearchCriteria;


interface ILottoMessenger
{
   	boolean startSynch();
   	boolean stopSync();
   	int getStatus();
  	Result4D getResult4DbyDrawNo(String name, String drawNo);
  	List<SearchCriteria> getSearchCriteria(String name);
  	List<Result4D> getPastResults(String name, int noOfDraw);
  	List<Result4D> getPastResultsByDates(String name, String fromDate, String toDate);
  	List<Result4D> getPastResultsAfterDate(String name, String fromDate);
  	
}