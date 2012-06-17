package com.mylotto.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Lotto search criteria. Implements Parcelable to allow transfer between processes.
 * 
 * @author MEKOH
 *
 */
public class SearchCriteria implements Parcelable {

	// Date in DD/MM/YYYY format
	public String drawDate;
	public String drawDay;
	public String drawNo;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel outParcel, int flags) {
		outParcel.writeString(drawDate);
		outParcel.writeString(drawDay);
		outParcel.writeString(drawNo);
	}

	/**
	 * Factory for creating instances of the Parcelable class.
	 */
	public static final Parcelable.Creator<SearchCriteria> CREATOR = new Parcelable.Creator<SearchCriteria>() {

		/**
		 * This method will be called to instantiate a SearchCriteria
		 * when a Parcel is received.
		 * All data fields which where written during the writeToParcel
		 * method should be read in the correct sequence during this method.
		 */
		@Override
		public SearchCriteria createFromParcel(Parcel in) {
			SearchCriteria criteria = new SearchCriteria();
			criteria.drawDate = in.readString();
			criteria.drawDay = in.readString();
			criteria.drawNo = in.readString();
			return criteria;
		}

		/**
		 * Creates an array of our Parcelable object.
		 */
		@Override
		public SearchCriteria[] newArray(int size) {
			return new SearchCriteria[size];
		}
	};

}
