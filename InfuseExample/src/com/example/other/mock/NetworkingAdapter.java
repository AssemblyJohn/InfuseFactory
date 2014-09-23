package com.example.other.mock;

import android.content.Context;

import com.factory.infuse.annotation.Singleton;

@Singleton
public class NetworkingAdapter {
	
	private Context mContext;
	
	private NetworkingAdapter(Context context) {
		mContext = context;
	}
}
