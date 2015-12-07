package com.mdedu.serverapi.message;


public class GradeResponse extends Response{
	private Grade[]grades;

	public Grade[] getGrades() {
		return grades;
	}

	public void setGrades(Grade[] grades) {
		this.grades = grades;
	}
	
	
}
