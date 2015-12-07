package com.mdedu;


import android.app.Application;

import com.mdedu.common.AppData;

public class MyApp extends Application {
	
	public void onCreate() {
		super.onCreate();
		String grade=null;
		AppData.getInstance().setGrade(grade);
		Integer gradeId=null;
		AppData.getInstance().setGradeId(gradeId);
		String subject=null;
		AppData.getInstance().setSubject(subject);
		Integer subjectId=null;
		AppData.getInstance().setSubjectId(subjectId);
	}
}
