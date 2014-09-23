package com.factory.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.factory.InfuseFactory;
import com.factory.infuse.Infuser;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseFragment extends Fragment {
	protected Infuser mInfuser; // Strong instance needed so it doesn't get GC'd
	
	@Override
	public void onCreate(Bundle savedOptionState) {
		mInfuser = InfuseFactory.getScopedInfuser(getActivity());
		
		mInfuser.infuseMembers(this);	 
		super.onCreate(savedOptionState);
	}
		
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mInfuser.infuseViews(this);
	}
}
