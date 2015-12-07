package com.mdedu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.mdedu.common.AppData;

public class SettingActivity   extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		
		String grade = AppData.getInstance().getGrade();
		
		String subject = AppData.getInstance().getSubject();
		
		if(grade==null){
			grade="请选择";
		}
		if(subject==null){
			subject="请选择";
		}
		
		TextView gradeView=(TextView)findViewById(R.id.grade);
		gradeView.setText("年级： "+grade);
		
		TextView subjectView=(TextView)findViewById(R.id.subject);
		subjectView.setText("科目： "+grade);
	}
}
