package com.factory.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.factory.InfuseFactory;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseActivityActionBar extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedOptionState) {
		InfuseFactory.getScopedInfuser(this).infuseMembers(this);
		
		super.onCreate(savedOptionState);
	}
	
	
	public void onSupportContentChanged() {
		super.onSupportContentChanged();
		
		InfuseFactory.getScopedInfuser(this).infuseViews(this);
	}
}
