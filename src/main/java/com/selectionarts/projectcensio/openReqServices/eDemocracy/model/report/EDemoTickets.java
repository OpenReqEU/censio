package com.selectionarts.projectcensio.openReqServices.eDemocracy.model.report;

import com.selectionarts.projectcensio.openReqServices.eDemocracy.model.Ticket;

public class EDemoTickets {
	
	private int votes_received;
	private int[] voted_by;
	
	private Ticket ticket;

	public EDemoTickets() {
		// TODO Auto-generated constructor stub
	}
	
	public int getVotes_received() {
		return votes_received;
	}

	public void setVotes_received(int votes_received) {
		this.votes_received = votes_received;
	}

	public int[] getVoted_by() {
		return voted_by;
	}

	public void setVoted_by(int[] voted_by) {
		this.voted_by = voted_by;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	
}
