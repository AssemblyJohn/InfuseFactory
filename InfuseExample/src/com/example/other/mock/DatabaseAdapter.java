package com.example.other.mock;

import android.content.Context;

import com.factory.infuse.annotation.Singleton;

@Singleton
public class DatabaseAdapter {
	
	private Context mContext;
	
	private DatabaseAdapter(Context context) {
		mContext = context;
	}
	
	
}
