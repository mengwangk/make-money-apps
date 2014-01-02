package com.mymobkit.datastore;

import java.util.logging.Logger;

import javax.inject.Singleton;

import com.googlecode.objectify.ObjectifyFactory;
import com.mymobkit.model.LoginUser;
import com.mymobkit.model.Phone;
import com.mymobkit.model.Workspace;

/**
 * Our version of ObjectifyFactory which integrates with Guice.  You could and convenience methods here too.
 *
 */
@Singleton
public class OfyFactory extends ObjectifyFactory
{
	protected static final Logger logger = Logger.getLogger(OfyFactory.class.getName());
	
	/**
	 * Register our entity types.
	 */
	public OfyFactory() {
		long time = System.currentTimeMillis();
		
		// Register classes
		this.register(LoginUser.class);
		this.register(Workspace.class);
		this.register(Phone.class);
		
		long millis = System.currentTimeMillis() - time;
		logger.info("Registration took " + millis + " millis");
	}

	@Override
	public Ofy begin() {
		return new Ofy(this);
	}
	
	
	@Override
	public <T> T construct(Class<T> type) {
		return super.construct(type);
	}

}