package com.example.other.ui.listener;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.example.infuseexample.R;
import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.InfuseView;

public class HideListener implements OnClickListener {
	private static final String TAG = "HIDE_LISTENER";
	
	@InfuseView(R.id.listView1)
	private ListView mToHide;
	
	@InfuseView(R.id.buttonHide)
	private Button mHide;
	
	@Infuse private ListListener mListener;
	
	@Override
	public void onClick(View v) {
		Log.d(TAG, "Hiding list!");
		
		if(mToHide.getVisibility() == View.INVISIBLE) {
			mHide.setText("Make invisible");
			mToHide.setVisibility(View.VISIBLE);
		} else {
			mHide.setText("Make visible");
			mToHide.setVisibility(View.INVISIBLE);
		}
		
		mListener.reactToHide();
	}

}
