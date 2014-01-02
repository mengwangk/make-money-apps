package com.mymobkit.datastore;


import static com.mymobkit.datastore.OfyService.ofy;

import com.googlecode.objectify.impl.LoaderImpl;

/**
 * Extend the Loader command with our own logic
 *
 */
public class OfyLoader extends LoaderImpl<OfyLoader>
{
	public OfyLoader(Ofy base) {
		super(base);
	}
	
}