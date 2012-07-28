package com.simpleblocker.ui.fragments.base;

import java.util.List;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;


/**
 * 
 * This activity is used as base of list activities or (fragments) in along
 * the app to have different commons features without the necessity to
 * implement the same code over and over again.
 */
public abstract class BaseListFragment<T> extends SherlockListFragment {
	
	public ArrayAdapter<T> myOwnAdapter;
	
	//Used to fix the index cursor when we delete items from the list
	private boolean fixIndexCursor = false;
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);													
		
	}	
	
	/**
	 * Get all defined info from the database and parse it for create
	 * one list only with the {@link String} info.
	 * @param dataInfoList
	 */
	public abstract void getDataInfo (List<T> dataInfoList);
	
	/**
	 * Refreshes the list to also can use properly the indexer and fastScroll
	 */
	protected void refreshList () {				
		
		/* To refresh the indexer we need to disable the fastScroll, 
		 * call to notifyDataSetChanged, re-create the index sections 
		 * and enable again the fastScroll.
		 * After that call to the function which fix a problem with the
		 * view which shows the section index.
		 */
		getListView().setFastScrollEnabled(false);
		myOwnAdapter.notifyDataSetChanged();					
		getListView().setFastScrollEnabled(true);
		jiggleWidth();
	}		
	
	/**
	 * This function fixes the known problem {@link http://code.google.com/p/
	 * android/issues/detail?id=9054}
	 * the patch was founded in {@link http://stackoverflow.com/questions/2912082/
	 * section-indexer-overlay-is-not-updating-as-the-adapters-data-changes}.
	 * 
	 * Is not really elegant, but it seems is the known way to fix it.
	 */
	private void jiggleWidth() {

	    ListView view = getListView();
	    if (view.getWidth() <= 0)
	        return;	   
	    int newWidth = fixIndexCursor ? view.getWidth() - 1 : view.getWidth() + 1;
	    ViewGroup.LayoutParams params = view.getLayoutParams();
	    params.width = newWidth;
	    view.setLayoutParams( params );

	    fixIndexCursor = !fixIndexCursor;
	}	

}
