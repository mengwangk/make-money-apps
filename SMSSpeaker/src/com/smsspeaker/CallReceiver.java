package com.smsspeaker;

import java.util.GregorianCalendar;

import com.smsspeaker.helper.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public final class CallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
		String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
			Intent targetIntent = new Intent(Constants.BROADCAST_ACTION_CALL_RECEIVED);
			InboundData inboundData = new InboundData(number, StringUtils.EMPTY, Constants.DATA_TYPE_CALL, new GregorianCalendar().getTime());
			inboundData.isNew = true;
			targetIntent.putExtra(Constants.PARAM_CALL, inboundData);
			context.sendBroadcast(targetIntent);
		}
		if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
			// do nothing
		}
		if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
			// do nothing
		}
	}
}