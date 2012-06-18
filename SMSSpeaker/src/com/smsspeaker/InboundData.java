package com.smsspeaker;

import java.io.Serializable;
import java.util.Date;

import com.smsspeaker.helper.StringUtils;


public final class InboundData implements Serializable {

	private static final long serialVersionUID = 7023095970238728553L;
	
	public Long id = 0l;
	public String subject = StringUtils.EMPTY;
	public String details = StringUtils.EMPTY;
	public String type = StringUtils.EMPTY;
	public Date timestamp;
	public boolean isNew = false;

	public InboundData() {
	}

	public InboundData(String subject, String details, String type, Date timestamp){
		this.subject = subject;
		this.timestamp = timestamp;
		this.details = details;
		this.type = type;
	}
	
}