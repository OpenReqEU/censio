package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

public class EdemoVotingDto {
	
	private int[] votes;
	
	public EdemoVotingDto(int[] votes) {
		super();
		this.votes = votes;
	}

	public EdemoVotingDto() {
		super();
	}

	public int[] getVotes() {
		return votes;
	}

	public void setVotes(int[] votes) {
		this.votes = votes;
	}
	
	
}
