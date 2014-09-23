package com.example.other.ui.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.BadTokenException;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.ScopedSingleton;

@ScopedSingleton
public class ListListener implements OnItemClickListener {
	private static final String TAG = "LIST_LISTENER";
	
	// As an alternative we can receive them in the constructor
	
	// This (global) context is nice but we can't display any UI (like dialogs)
	@Infuse private Context mContext;
	
	// That's why we use the parent!
	@Infuse private Activity mActivity;
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "Item clicked: " + position);
		
		// Should throw an exception
		try {
			buildDialog(mContext, position);
		} catch(BadTokenException e) {
			e.printStackTrace(); // Should get this
		} catch(Exception e) { 
			e.printStackTrace(); 
		}
		
		// Should still throw an exception
		try {
			buildDialog(view.getContext(), position);
		} catch(BadTokenException e) {
			e.printStackTrace(); // Should get this
		} catch(Exception e) { 
			e.printStackTrace(); 
		}
				
		
		// On the other hand, this should work
		buildDialog(mActivity, position);
	}
	
	public void reactToHide() {
		Log.d(TAG, "We were notified that the list was hidden!");
	}
	
	private void buildDialog(Context context, int position) {
		AlertDialog.Builder bld = new AlertDialog.Builder(context);
		
		bld.setTitle("Pressed at position: " + position);
		bld.setPositiveButton("Ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		bld.show();
	}
}
