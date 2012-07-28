package com.simpleblocker.data.models;


@SuppressWarnings("serial")
public final class CallLog extends Contact {
	
	private String phoneNo;
	private int callType;
	private boolean isWildCard;
	
	/**
	 * 
	 * @param contactId
	 * @param contactName
	 * @param phoneNo
	 * @param callType
	 */
	public CallLog(String contactId, String contactName, String phoneNo, int callType){
		this.contactId = contactId;
		this.contactName = contactName;
		this.phoneNo = phoneNo;
		this.callType = callType;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public int getCallType() {
		return callType;
	}

	public boolean isWildCard() {
		return isWildCard;
	}

	public void setWildCard(boolean isWildCard) {
		this.isWildCard = isWildCard;
	}
	
	@Override
	public String toString() {
		return "CallLog [phoneNumber=" + phoneNo + ", callType=" + callType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + callType;
		result = prime * result + ((phoneNo == null) ? 0 : phoneNo.hashCode());
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
		CallLog other = (CallLog) obj;
		if (callType != other.callType)
			return false;
		if (phoneNo == null) {
			if (other.phoneNo != null)
				return false;
		} else if (!phoneNo.equals(other.phoneNo))
			return false;
		return true;
	}

	
	

}
