package com.mymobkit.model;

/*
 * {
    "PhoneNumber": null,
    "ReceivedDate": "0001-01-01T00:00:00",
    "Timezone": null,
    "Content": null,
    "MessageType": 0,
    "TotalPiece": 0,
    "CurrentPiece": 0,
    "DeliveryStatus": 0,
    "DestinationReceivedDate": "0001-01-01T00:00:00",
    "ValidityTimestamp": "0001-01-01T00:00:00",
    "Index": 0,
    "MessageStatusType": 0,
    "ReferenceNo": 0,
    "SourcePort": 0,
    "DestinationPort": 0,
    "GatewayId": null,
    "TotalPieceReceived": 1,
    "Status": 0,
    "RawMessage": null,
    "Indexes": [],
    "DataBytes": [],
    "ServiceCentreAddress": null,
    "ServiceCentreAddressType": 0
  }
*/
public final class Incoming extends ModelBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8427549216308408206L;
	private String id;
	private String msgContent;
	private String dateCreated;
	private String dateModified;

}
