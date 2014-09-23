package com.example.other.mock;

import com.factory.infuse.annotation.Singleton;

import android.content.Context;

@Singleton
public class PreferencesAdapter {
	private Context mContext;
	
	private PreferencesAdapter(Context context) {
		mContext = context;
	}
}
