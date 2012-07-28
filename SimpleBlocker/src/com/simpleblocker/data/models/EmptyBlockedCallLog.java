package com.simpleblocker.data.models;

@SuppressWarnings("serial")
public final class EmptyBlockedCallLog extends BlockedCallLog {

	@Override
	public boolean isEmpty() {
		return true;
	}
	
}
