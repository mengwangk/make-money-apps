package com.mymobkit.net.provider;

public interface Processor<THeader, TParam, TResult> {
	
	public TResult process(THeader header, TParam command);

}
