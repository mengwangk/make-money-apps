package com.simpleblocker.data.models;

import java.io.Serializable;
import java.util.Date;

public class BlockedCallLog extends Id implements Serializable {
	
	private static final long serialVersionUID = 3429264144478459611L;
	private String contactName;
	private String phoneNo;
	private String timestamp;
	
	
	/**
	 * Constructor
	 */
	protected BlockedCallLog(){
		
	}
	
	/**
	 * Constructor
	 * 
	 * @param contactName
	 * @param phoneNo
	 * @param timestamp
	 */
	public BlockedCallLog(String contactName, String phoneNo, String timestamp) {
		super();
		this.contactName = contactName;
		this.phoneNo = phoneNo;
		this.timestamp = timestamp;
	}


	public String getContactName() {
		return contactName;
	}


	public String getPhoneNo() {
		return phoneNo;
	}


	public String getTimestamp() {
		return timestamp;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlockedCallLog other = (BlockedCallLog) obj;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		if (phoneNo == null) {
			if (other.phoneNo != null)
				return false;
		} else if (!phoneNo.equals(other.phoneNo))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "BlockedCallLog [contactName=" + contactName + ", phoneNo=" + phoneNo + ", timestamp=" + timestamp + "]";
	}


	
	
	
	

}
