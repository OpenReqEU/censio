package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report;

public class EDemoVotes {

	private EDemoTickets[] tickets;
	private EDemoCandidates[] candidates;
	
	public EDemoVotes() {
		// TODO Auto-generated constructor stub
	}

	public EDemoTickets[] getTickets() {
		return tickets;
	}

	public void setTickets(EDemoTickets[] tickets) {
		this.tickets = tickets;
	}

	public EDemoCandidates[] getCandidates() {
		return candidates;
	}

	public void setCandidates(EDemoCandidates[] candidates) {
		this.candidates = candidates;
	}
	
}
