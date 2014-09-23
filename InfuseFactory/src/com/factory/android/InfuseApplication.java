package com.factory.android;

import android.app.Application;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseApplication extends Application {
	protected static Infuser mInfuser = InfuseFactory.getInfuser();
	
	public void onCreate() {
		super.onCreate();
		
		// Populate factory context
		InfuseFactory.setContext(this);
	}
}
