package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

public class EDemoParticipationDto {

	private int user_id;
	private String role;
	private int project_id;
	private String candidate_summary;
	
	public EDemoParticipationDto() {
		super();
		this.candidate_summary = "abc";
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	public String getCandidate_summary() {
		return candidate_summary;
	}
	public void setCandidate_summary(String candidate_summary) {
		this.candidate_summary = candidate_summary;
	}
	
	
}
