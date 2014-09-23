package com.factory.java;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;

public abstract class InfuseObject {
	protected Infuser mInfuser = InfuseFactory.getInfuser(); 
	
	public InfuseObject() {
		mInfuser.infuseMembers(this);
	}
}
