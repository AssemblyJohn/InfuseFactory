package com.example.other.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.infuseexample.R;
import com.factory.InfuseFactory;
import com.factory.infuse.annotation.InfuseView;

public class TestListAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	
	public TestListAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	String items[] = new String[] {
		"ONE", "TWO", "THREE", "FOUR",
	};
	
	@Override
	public int getCount() {
		return items.length;
	}

	@Override
	public Object getItem(int position) {
		return items[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView; 
		
		if(v == null) {
			v = mInflater.inflate(R.layout.test_list_item, parent, false);
			
			ViewHolder holder = new ViewHolder();
			InfuseFactory.getInfuser().infuseViews(holder, v);
			v.setTag(holder);
		}
		
		ViewHolder holder = (ViewHolder) v.getTag();
		
		holder.text.setText((String)getItem(position));
		
		return v;
	}

	private class ViewHolder {
		@InfuseView(R.id.textViewList1)
		TextView text;
	} 
}
