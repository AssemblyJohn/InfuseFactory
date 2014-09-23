package com.factory.infuse.internal;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;

public class ViewResolver {
	private Activity mActivity;
	private Fragment mFragment;
	private View mView;
	
	public ViewResolver(Activity activity) {
		mActivity = activity;
	}
	
	public ViewResolver(Fragment fragment) {
		mFragment = fragment;
	}
	
	public ViewResolver(View view) {
		mView = view;
	}
	
	public View resolveView(int id) {
		if(mActivity != null) {
			return mActivity.findViewById(id);
		} else if(mFragment != null) {
			return mFragment.getView().findViewById(id);
		} else {
			return mView.findViewById(id);
		}
	}
}
