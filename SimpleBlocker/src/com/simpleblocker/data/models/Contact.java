package com.simpleblocker.data.models;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;


/**
 * 
 */
public class Contact extends Id {
	
	protected String contactId = "";
	protected String contactName = "";
	
	private List<Phone> phoneList = new ArrayList<Phone>(1);
	
	
	//-------------		Getters & Setters		------------------------------//	
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	public List<Phone> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<Phone> phoneList) {
		this.phoneList = phoneList;
	}
	
	//------------------------------------------------------------------------//
	
	/**
	 * Constructor without parameters
	 */
	public Contact () {		
		super();
	}
	

	/**
	 * Complete constructor with two essential properties, contact id and contact name
	 * 
	 * @param contactId
	 * @param contactName
	 */
	public Contact (String contactId, String contactName) {
		
		super();
		this.contactId = contactId;
		if (!TextUtils.isEmpty(contactName))
			this.contactName = contactName;
	}
	@Override
	public String toString() {
		return "Contact [contactId=" + contactId + ", contactName=" + contactName + ", phoneList=" + phoneList + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contactId == null) ? 0 : contactId.hashCode());
		result = prime * result + ((contactName == null) ? 0 : contactName.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		/*if (!super.equals(obj))
			return false;*/
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (contactId == null) {
			if (other.contactId != null)
				return false;
		} else if (!contactId.equals(other.contactId))
			return false;
		if (contactName == null) {
			if (other.contactName != null)
				return false;
		} else if (!contactName.equals(other.contactName))
			return false;
		return true;
	}
	

	
	
	
	

}
