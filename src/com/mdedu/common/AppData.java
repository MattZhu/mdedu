package com.mdedu.common;

public class AppData {
	
	private static AppData instance=new AppData();
	private String subject;
	private String grade;
	
	private Integer gradeId;
	
	private Integer subjectId;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public static AppData getInstance() {
		return instance;
	}

	
	
}
