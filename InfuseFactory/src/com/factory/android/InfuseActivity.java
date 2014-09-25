package com.factory.android;

import android.app.Activity;
import android.os.Bundle;

import com.factory.InfuseFactory;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedOptionState) {
		InfuseFactory.getScopedInfuser(this).infuseMembers(this);	
		
		super.onCreate(savedOptionState);
	}
	
	
	public void onContentChanged() {
		super.onContentChanged();
		
		InfuseFactory.getScopedInfuser(this).infuseViews(this);
	}
}
