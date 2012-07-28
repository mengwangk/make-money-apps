package com.simpleblocker.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.simpleblocker.R;
import com.simpleblocker.ui.activities.base.BaseFragmentActivity;
import com.simpleblocker.ui.fragments.BlockedListFragment;
import com.simpleblocker.ui.fragments.CallLogFragment;
import com.simpleblocker.ui.fragments.ContactsFragment;
import com.simpleblocker.utils.AppConfig;

public class ListActivityHolder extends BaseFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		super.homeToUp(true);

		setContentView(R.layout.listactivityholder);

		Intent intent = getIntent();
		if (intent != null) {
			int fragmentType = intent.getIntExtra(AppConfig.FRAGMENT_TYPE, 0);

			if (fragmentType == AppConfig.FragmentType.GET_CONTACTS.ordinal()) {
				getSupportActionBar().setTitle(R.string.label_title_get_contacts);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ContactsFragment fragment = new ContactsFragment();
				ft.replace(R.id.holder, fragment);
				ft.commit();
			} else if (fragmentType == AppConfig.FragmentType.GET_CALL_LOGS.ordinal()) {
				getSupportActionBar().setTitle(R.string.label_title_get_call_logs);
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				CallLogFragment fragment = new CallLogFragment();
				ft.replace(R.id.holder, fragment);
				ft.commit();
			} else if (fragmentType == AppConfig.FragmentType.GET_BLOCKED_CONTACTS.ordinal()) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				BlockedListFragment fragment = new BlockedListFragment();
				ft.replace(R.id.holder, fragment);
				ft.commit();
			} 
		}
	}
	
}
