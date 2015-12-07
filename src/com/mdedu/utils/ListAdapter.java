package com.mdedu.utils;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class ListAdapter<T> extends BaseAdapter {
	private List<T> data;
	private ListItemViewCreator<T> viewCreator;
	private Context context;
	
	public ListAdapter(Context context, ListItemViewCreator<T> creator) {
		this.context = context;
		this.viewCreator = creator;
	}

	public int getCount() {
		if (data != null) {
			return data.size();
		}	
		return 0;

	}

	public T getItem(int position) {
		return data.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	/**
	 * @return the data
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * @param list
	 *            the data to set
	 */
	public void setData(List<T> list) {
		
		if(list==null){
//			if(context instanceof LoadingActivity){
//				LoadingActivity activity=(LoadingActivity)context;
//				activity.showToast();
//			}
		}else
		{
			this.data = list;
			this.notifyDataSetChanged();
		
		}
	}
	public View getView(int position, View convertView, ViewGroup parent) {
//		Log.d("ListAdapter","get View for "+position);
		return viewCreator.createOrUpdateView(data.get(position), convertView,
				position, parent);
	}

}