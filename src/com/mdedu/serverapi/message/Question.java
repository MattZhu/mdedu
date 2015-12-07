package com.mdedu.serverapi.message;

public class Question {
	private Integer id;
	
	private String title;
	
	private String type;
	
	private String options;
	
	private String correctAnswer;
	
	private String imagePath;
	
	private Integer chId;
	
	private String explaination;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(String options) {
		this.options = options;
	}

	/**
	 * @return the correctAnswer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * @param correctAnswer the correctAnswer to set
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
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

	/**
	 * @return the chId
	 */
	public Integer getChId() {
		return chId;
	}

	/**
	 * @param chId the chId to set
	 */
	public void setChId(Integer chId) {
		this.chId = chId;
	}

	/**
	 * @return the explaination
	 */
	public String getExplaination() {
		return explaination;
	}

	/**
	 * @param explaination the explaination to set
	 */
	public void setExplaination(String explaination) {
		this.explaination = explaination;
	}

	
}
