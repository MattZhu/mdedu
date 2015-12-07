package com.mdedu.serverapi.message;

public class Chapter {
	private int id;
	private String name;
	private int seq;
	private Video[] videos;
	private Chapter[] children;
	private Question[] questions;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
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
	 * @return the seq
	 */
	public int getSeq() {
		return seq;
	}
	/**
	 * @param seq the seq to set
	 */
	public void setSeq(int seq) {
		this.seq = seq;
	}
	/**
	 * @return the videos
	 */
	public Video[] getVideos() {
		return videos;
	}
	/**
	 * @param videos the videos to set
	 */
	public void setVideos(Video[] videos) {
		this.videos = videos;
	}
	/**
	 * @return the children
	 */
	public Chapter[] getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(Chapter[] children) {
		this.children = children;
	}
	/**
	 * @return the questions
	 */
	public Question[] getQuestions() {
		return questions;
	}
	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(Question[] questions) {
		this.questions = questions;
	}
	
	
}
