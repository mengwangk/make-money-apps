package com.simpleblocker.data.models;


/**
 * 
 *
 */
public final class Phone extends Id {
	
	private Contact contact;
	private String contactPhone;
	private String contactPhoneType;
	
	
	//-------	Getters & Setters	--------------//
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	public String getContactPhoneType() {
		return contactPhoneType;
	}
	
	public void setContactPhoneType(String contactPhoneType) {
		this.contactPhoneType = contactPhoneType;
	}
	
	//---------------------------------------------//
	
	
	
	
	/**
	 * @param contact
	 * @param contactPhone
	 * @param contactPhoneType
	 */
	public Phone (final Contact contact, final String contactPhone, final String contactPhoneType) {
		super();
		this.contact = contact;
		this.contactPhone = contactPhone;
		this.contactPhoneType = contactPhoneType;
	}
	
	@Override
	public String toString() {
		return "Phone [contact=" + contact + ", contactPhone=" + contactPhone + ", contactPhoneType=" + contactPhoneType + "]";
	}

}