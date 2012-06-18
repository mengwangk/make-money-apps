package com.smsspeaker;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Receive SMS broadcast.
 * 
 */
public final class SmsReceiver extends BroadcastReceiver {


	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		if (bundle != null) {
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			InboundData inboundMessage = new InboundData();
			inboundMessage.type = Constants.DATA_TYPE_SMS;
			inboundMessage.isNew = true;
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				inboundMessage.subject = msgs[i].getOriginatingAddress();
				inboundMessage.details += msgs[i].getMessageBody();
				inboundMessage.timestamp = new Date(msgs[i].getTimestampMillis());
			}
			
			Intent targetIntent = new Intent(Constants.BROADCAST_ACTION_SMS_RECEIVED);
			targetIntent.putExtra(Constants.PARAM_MSG, inboundMessage);
			context.sendBroadcast(targetIntent);
			
			/*
			Intent targetIntent = new Intent(context, StartMenuActivity.class);
			targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			targetIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			targetIntent.putExtra(Constants.PARAM_MSG, msg);
			context.startActivity(targetIntent);
			*/
		}
	}
}
