package com.mdedu.activity;


import android.os.Bundle;

import com.mdedu.R;

public class MyCourseActivity extends BaseActivity {

	/* (non-Javadoc)
	 * @see com.mdedu.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		initializeViews();
	}
	private void initializeViews() {
		this.setContentView(R.layout.activity_my_courses);
		
	}
	
}
