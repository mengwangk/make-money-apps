package com.mymobkit.model;


/**
 * Sample message content in JSON
 * 
 * {
  "DestinationAddress": "0192292309",
  "Protocol": 0,
  "Flash": false,
  "SourcePort": -1,
  "DestinationPort": -1,
  "ReferenceNo": [],
  "Indexes": [],
  "SaveSentMessage": false,
  "RawMessage": false,
  "LongMessageOption": 3,
  "ReplyPath": "",
  "ServiceCenterNumber": null,
  "DataCodingScheme": -1,
  "Content": "testing message",
  "ContentLength": 0,
  "StatusReportRequest": 0,
  "ValidityPeriod": 24,
  "CustomValidityPeriod": 60,
  "DcsMessageClass": 0,
  "Binary": false,
  "DataBytes": null,
  "GatewayId": null,
  "QueuePriority": 0,
  "Identifier": null,
  "ScheduledDeliveryDate": "2013-11-17T20:38:35.4311349+08:00",
  "Persisted": false
}
 */
public final class Outgoing extends ModelBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7435972283218778849L;
	
	private String id;
	private String msgContent;
	private String msgType;
	private String status;
	private String dateCreated;
	private String dateModified;
	
	
	public Outgoing(String msgContent) {
		super();
		this.msgContent = msgContent;
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getMsgContent() {
		return msgContent;
	}



	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}



	public String getMsgType() {
		return msgType;
	}



	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getDateCreated() {
		return dateCreated;
	}



	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}



	public String getDateModified() {
		return dateModified;
	}



	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((msgContent == null) ? 0 : msgContent.hashCode());
		result = prime * result + ((msgType == null) ? 0 : msgType.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outgoing other = (Outgoing) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateModified == null) {
			if (other.dateModified != null)
				return false;
		} else if (!dateModified.equals(other.dateModified))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (msgContent == null) {
			if (other.msgContent != null)
				return false;
		} else if (!msgContent.equals(other.msgContent))
			return false;
		if (msgType == null) {
			if (other.msgType != null)
				return false;
		} else if (!msgType.equals(other.msgType))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Outgoing [id=" + id + ", msgContent=" + msgContent + ", msgType=" + msgType + ", status=" + status + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + "]";
	}
	
	
}
