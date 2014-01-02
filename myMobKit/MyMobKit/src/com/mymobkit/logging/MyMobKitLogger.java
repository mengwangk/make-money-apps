package com.mymobkit.logging;

abstract public class MyMobKitLogger 
{
	// Creation & retrieval methods:
	public static MyMobKitLogger getLogger(String name)
	{
		return MyMobKitLoggerDefault.getLogger(name);
	}
	
	public static MyMobKitLogger getLogger(Class<?> clazz)
	{
		return getLogger(clazz.getName());
	}
	
	public boolean isEnabled()
	{
		return true;
	}

	// printing methods:
	abstract public void trace(Object message);
	abstract public void debug(Object message);
	abstract public void info(Object message);
	abstract public void warn(Object message);
	abstract public void error(Object message);
	abstract public void fatal(Object message);

	abstract public void trace(Object message, Throwable t);
	abstract public void debug(Object message, Throwable t);
	abstract public void info(Object message, Throwable t);
	abstract public void warn(Object message, Throwable t);
	abstract public void error(Object message, Throwable t);
	abstract public void fatal(Object message, Throwable t);
}