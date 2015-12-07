package com.mdedu.serverapi.message;



public class CourseResponse extends Response {

	
	private Course[]courses;
	
	private Grade[] grades;
	
	private Subject[] subjects;

	public Course[] getCourses() {
		return courses;
	}

	public void setCourses(Course[] courses) {
		this.courses = courses;
	}

	/**
	 * @return the grades
	 */
	public Grade[] getGrades() {
		return grades;
	}

	/**
	 * @param grades the grades to set
	 */
	public void setGrades(Grade[] grades) {
		this.grades = grades;
	}

	/**
	 * @return the subjects
	 */
	public Subject[] getSubjects() {
		return subjects;
	}

	/**
	 * @param subjects the subjects to set
	 */
	public void setSubjects(Subject[] subjects) {
		this.subjects = subjects;
	}
	
	
}