package com.factory.android;

import android.app.Activity;
import android.os.Bundle;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseActivity extends Activity {
	protected Infuser mInfuser; // Strong instance needed so it doesn't get GC'd
	
	@Override
	protected void onCreate(Bundle savedOptionState) {
		mInfuser = InfuseFactory.getScopedInfuser(this);
		
		mInfuser.infuseMembers(this);	 
		super.onCreate(savedOptionState);
	}
	
	
	public void onContentChanged() {
		super.onContentChanged();
		mInfuser.infuseViews(this);
	}
}
