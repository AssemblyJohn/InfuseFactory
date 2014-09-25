package com.factory.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.factory.InfuseFactory;

/**
 * 
 * @author Lazu Ioan-Bogdan
 *
 */
public class InfuseFragment extends Fragment {
	@Override
	public void onCreate(Bundle savedOptionState) {
		InfuseFactory.getScopedInfuser(getActivity()).infuseMembers(this);
		
		super.onCreate(savedOptionState);
	}
		
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		InfuseFactory.getScopedInfuser(getActivity()).infuseViews(this);
	}
}
