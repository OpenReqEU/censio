package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report;

public class EDemoReport {

	private EDemoVotes votes;
	private EDemoSchedule schedule;
	private EDemoParticipation participations;
	
	public EDemoReport() {
		// TODO Auto-generated constructor stub
	}

	public EDemoVotes getVotes() {
		return votes;
	}

	public void setVotes(EDemoVotes votes) {
		this.votes = votes;
	}

	public EDemoSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(EDemoSchedule schedule) {
		this.schedule = schedule;
	}

	public EDemoParticipation getParticipations() {
		return participations;
	}

	public void setParticipations(EDemoParticipation participations) {
		this.participations = participations;
	}
	
	
}
