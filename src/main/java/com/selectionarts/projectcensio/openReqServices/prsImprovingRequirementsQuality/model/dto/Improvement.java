package com.selectionarts.projectcensio.openReqServices.prsImprovingRequirementsQuality.model.dto;

public class Improvement {

    private String description;
    private Integer index_end;
    private Integer index_start;
    private String language_construct;
    private String text;
    private String title;
    private String color;
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIndex_end() {
		return index_end;
	}
	public void setIndex_end(Integer index_end) {
		this.index_end = index_end;
	}
	public Integer getIndex_start() {
		return index_start;
	}
	public void setIndex_start(Integer index_start) {
		this.index_start = index_start;
	}
	public String getLanguage_construct() {
		return language_construct;
	}
	public void setLanguage_construct(String language_construct) {
		this.language_construct = language_construct;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
}
