package com.mylotto.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import com.mylotto.helper.DateUtils;
import com.mylotto.helper.StringUtils;

/**
 * 4d results class. Implements Parcelable interface so that it can be transferred
 * across different processes.
 * 
 * @author MEKOH
 *
 */
public final class Result4D extends BaseResult { 
	
	public String drawNo = StringUtils.EMPTY;
	public Date drawDate = null;
	public String drawDay = StringUtils.EMPTY;
	
	public String firstPrize = StringUtils.EMPTY;
	public String secondPrize = StringUtils.EMPTY;
	public String thirdPrize = StringUtils.EMPTY;
	
	public List<String> specialNumbers = new ArrayList<String>(10);
	public List<String> consolationNumbers  = new ArrayList<String>(10);
	
	

	/**
	 * Method which will give additional hints how to process
	 * the parcel. For example there could be multiple
	 * implementations of an Interface which extends the Parcelable
	 * Interface. When such a parcel is received you can use
	 * this to determine which object you need to instantiate.
	 */
	@Override
	public int describeContents() {
		return 0;	// nothing special about our content
	}
	
	/**
	 * Method which will be called when this object should be
	 * marshalled to a Parcelable object.
	 * Add all required data fields to the parcel in this
	 * method.
	 */
	@Override
	public void writeToParcel(Parcel outParcel, int flags) {
		outParcel.writeString(drawNo);
		outParcel.writeString(DateUtils.formatDate(drawDate));
		outParcel.writeString(drawDay);
		outParcel.writeString(firstPrize);
		outParcel.writeString(secondPrize);
		outParcel.writeString(thirdPrize);
		
		for (String no: specialNumbers) {
			outParcel.writeString(no);
		}
		
		for (String no: consolationNumbers) {
			outParcel.writeString(no);
		}
	}
	
	
	/**
	 * Factory for creating instances of the Parcelable class.
	 */
	public static final Parcelable.Creator<Result4D> CREATOR = new Parcelable.Creator<Result4D>() {
		
		/**
		 * This method will be called to instantiate a Result4D
		 * when a Parcel is received.
		 * All data fields which where written during the writeToParcel
		 * method should be read in the correct sequence during this method.
		 */
		@Override
		public Result4D createFromParcel(Parcel in) {
			Result4D result = new Result4D();
			result.drawNo = in.readString();
			result.drawDate = DateUtils.parseDate(in.readString());
			result.drawDay = in.readString();
			result.firstPrize = in.readString();
			result.secondPrize = in.readString();
			result.thirdPrize = in.readString();
			
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			result.specialNumbers.add(in.readString());
			
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());
			result.consolationNumbers.add(in.readString());

			return result;
		}

		/**
		 * Creates an array of our Parcelable object.
		 */
		@Override
		public Result4D[] newArray(int size) {
			return new Result4D[size];
		}
	};
}
