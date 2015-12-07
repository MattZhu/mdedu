package com.mdedu;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity  extends TabActivity  {
	private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int t = getIntent().getIntExtra("tab", 2);
		tabHost = getTabHost(); // The activity TabHost

		tabHost.getTabWidget().setBackgroundColor(Color.LTGRAY);
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		createTab(inflater,R.string.mycache,R.drawable.mycache,"mycache",MyCacheActivity.class);
		createTab(inflater,R.string.playhis,R.drawable.video,"playhis",PlayHisActivity.class);
		createTab(inflater,R.string.home,R.drawable.home,"home",HomeActivity.class);
		createTab(inflater,R.string.practice,R.drawable.questions,"practice",PracticeActivity.class);
		createTab(inflater,R.string.setting,R.drawable.setting,"setting",SettingActivity.class);

		tabHost.setCurrentTab(t);
	}

	private void createTab(LayoutInflater inflater,int textId,int drawId,String name,Class<?> clazz) {

		View menuItem  = inflater.inflate(R.layout.menuitem, null);
		TextView menuText = (TextView) menuItem.findViewById(R.id.menuText);
		menuText.setText(textId);
		ImageView menuImg=(ImageView)menuItem.findViewById(R.id.menuImg);
		menuImg.setImageResource(drawId);
		Intent intent = new Intent().setClass(this,clazz);
		TabHost.TabSpec spec = tabHost.newTabSpec(name).setIndicator(menuItem)
				.setContent(intent);
		tabHost.addTab(spec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
