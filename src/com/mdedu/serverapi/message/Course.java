package com.mdedu.serverapi.message;

public class Course {
	 private Long id;
	    private String name;
	    private Long subjectId;
	    private Long gradeId;
	    private String publisher;
	    private String imagePath;
		/**
		 * @return the id
		 */
		public Long getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(Long id) {
			this.id = id;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the subjectId
		 */
		public Long getSubjectId() {
			return subjectId;
		}
		/**
		 * @param subjectId the subjectId to set
		 */
		public void setSubjectId(Long subjectId) {
			this.subjectId = subjectId;
		}
		/**
		 * @return the gradeId
		 */
		public Long getGradeId() {
			return gradeId;
		}
		/**
		 * @param gradeId the gradeId to set
		 */
		public void setGradeId(Long gradeId) {
			this.gradeId = gradeId;
		}
		/**
		 * @return the publisher
		 */
		public String getPublisher() {
			return publisher;
		}
		/**
		 * @param publisher the publisher to set
		 */
		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}
		/**
		 * @return the imagePath
		 */
		public String getImagePath() {
			return imagePath;
		}
		/**
		 * @param imagePath the imagePath to set
		 */
		public void setImagePath(String imagePath) {
			this.imagePath = imagePath;
		}
		
	    
}
