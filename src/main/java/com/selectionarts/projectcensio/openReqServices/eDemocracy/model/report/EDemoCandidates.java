package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report;

public class EDemoCandidates {

	private int votes_received;
	private EDemoCandidate candidate;
	
	public EDemoCandidates() {
		// TODO Auto-generated constructor stub
	}

	public int getVotes_received() {
		return votes_received;
	}

	public void setVotes_received(int votes_received) {
		this.votes_received = votes_received;
	}

	public EDemoCandidate getCandidate() {
		return candidate;
	}

	public void setCandidate(EDemoCandidate candidate) {
		this.candidate = candidate;
	}
}
