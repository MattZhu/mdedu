package com.mdedu.utils;

import android.view.View;
import android.view.ViewGroup;

public interface ListItemViewCreator<T> {

	public View createOrUpdateView(T item, View convertView,int position, ViewGroup parent);
}
