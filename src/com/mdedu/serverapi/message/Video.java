package com.mdedu.serverapi.message;

public class Video {
	private Integer id;

	private String name;

	private String savedName;

	private Integer chId;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the savedName
	 */
	public String getSavedName() {
		return savedName;
	}

	/**
	 * @param savedName
	 *            the savedName to set
	 */
	public void setSavedName(String savedName) {
		this.savedName = savedName;
	}

	/**
	 * @return the chId
	 */
	public Integer getChId() {
		return chId;
	}

	/**
	 * @param chId
	 *            the chId to set
	 */
	public void setChId(Integer chId) {
		this.chId = chId;
	}
}
