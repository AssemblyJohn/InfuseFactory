package com.example.infuseexample;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.other.mock.DatabaseAdapter;
import com.example.other.mock.NetworkingAdapter;
import com.example.other.mock.PreferencesAdapter;
import com.example.other.ui.adapter.TestListAdapter;
import com.example.other.ui.listener.HideListener;
import com.example.other.ui.listener.ListListener;
import com.factory.android.InfuseActivity;
import com.factory.infuse.annotation.Infuse;
import com.factory.infuse.annotation.InfuseView;
import com.factory.infuse.annotation.bindings.BindAdapter;
import com.factory.infuse.annotation.bindings.BindOnClick;
import com.factory.infuse.annotation.bindings.BindOnItemClick;

public class MainActivity extends InfuseActivity {
	private static final String TAG = "MAIN_ACTIVITY";
	
	@BindAdapter(TestListAdapter.class)
	@BindOnItemClick(ListListener.class)
	@InfuseView(R.id.listView1)
	private ListView mListView;
	
	@BindOnClick(value = HideListener.class, id = R.id.buttonHide)
	private Button mHide;
	
	@InfuseView(R.id.textView1)
	private TextView mHelloWorld;
	
	@InfuseView(R.id.editText1)
	private EditText mText;
	
	@BindOnClick(value = GetTextListener.class, id = R.id.buttonText)
	private Button mGetText;
	
	// Infuse other utils that are used globally by the application
	@Infuse private DatabaseAdapter mDbAdapter;
	@Infuse private NetworkingAdapter mNetAdapter;
	@Infuse private PreferencesAdapter mPrefAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.d(TAG, "onCreate()");
	}
	
	@Override
	public void onContentChanged() {
		super.onContentChanged();
		
		Log.d(TAG, "onContentChanged");
		
		mHelloWorld.setText("Goodbye world!"); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private static class GetTextListener implements OnClickListener {		
		
		@InfuseView(R.id.editText1)
		private TextView mText;
		
		@InfuseView(R.id.buttonText)
		private Button mGetText;
		
		@Override
		public void onClick(View v) {
			String text = mText.getText().toString();
			
			Log.d(TAG, "Text got: " + text);
			if(text != null) mGetText.setText(text);
		}
	}
}
