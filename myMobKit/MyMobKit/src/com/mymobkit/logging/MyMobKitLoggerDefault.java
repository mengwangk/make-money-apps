package com.mymobkit.logging;

import android.util.Log;


public class MyMobKitLoggerDefault extends MyMobKitLogger
{
	protected String tag;

	// Creation & retrieval methods:
	public static MyMobKitLogger getLogger(String name)
	{
		return new MyMobKitLoggerDefault(name);
	}
	
	protected MyMobKitLoggerDefault(String name)
	{
		this.tag = name;
	}

	// printing methods:
	public void trace(Object message)
	{
		Log.v(this.tag, message.toString());
	}
	
	public void debug(Object message)
	{
		Log.d(this.tag, message.toString());
	}
	
	public void info(Object message)
	{
		Log.i(this.tag, message.toString());
	}
	
	public void warn(Object message)
	{
		Log.w(this.tag, message.toString());
	}
	
	public void error(Object message)
	{
		Log.e(this.tag, message.toString());
	}
	
	public void fatal(Object message)
	{
		Log.wtf(this.tag, message.toString());
	}

	public void trace(Object message, Throwable t)
	{
		Log.v(this.tag, message.toString(), t);
	}
	
	public void debug(Object message, Throwable t)
	{
		Log.d(this.tag, message.toString(), t);
	}
	
	public void info(Object message, Throwable t)
	{
		Log.i(this.tag, message.toString(), t);
	}
	
	public void warn(Object message, Throwable t)
	{
		Log.w(this.tag, message.toString(), t);
	}
	
	public void error(Object message, Throwable t)
	{
		Log.e(this.tag, message.toString(), t);
	}
	
	public void fatal(Object message, Throwable t)
	{
		Log.wtf(this.tag, message.toString(), t);
	}
}
