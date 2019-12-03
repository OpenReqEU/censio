package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

public class Ticket {
	
	private int id;
	private String title;
	private String url;
	private int external_id;
	private String description;
	
	public Ticket() {
	}

	public Ticket(int id, String title, String url, int external_id, String description) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.external_id = external_id;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getExternal_id() {
		return external_id;
	}
	public void setExternal_id(int external_id) {
		this.external_id = external_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
