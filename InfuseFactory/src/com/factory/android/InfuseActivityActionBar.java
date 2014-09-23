package com.factory.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseActivityActionBar extends ActionBarActivity {
	protected Infuser mInfuser; // Strong instance needed so it doesn't get GC'd
	
	@Override
	protected void onCreate(Bundle savedOptionState) {
		mInfuser = InfuseFactory.getScopedInfuser(this);
		
		mInfuser.infuseMembers(this);	 
		super.onCreate(savedOptionState);
	}
	
	
	public void onSupportContentChanged() {
		super.onSupportContentChanged();
		mInfuser.infuseViews(this);
	}
}
