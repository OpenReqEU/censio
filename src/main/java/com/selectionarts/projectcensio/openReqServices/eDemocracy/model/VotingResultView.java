package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

public class VotingResultView {

	private int yesVotes;
	private int noVotes;
	private int notVoted;
	
	public VotingResultView() {
		// TODO Auto-generated constructor stub
	}

	public VotingResultView(int yesVotes, int noVotes, int notVoted) {
		super();
		this.yesVotes = yesVotes;
		this.noVotes = noVotes;
		this.notVoted = notVoted;
	}

	public int getYesVotes() {
		return yesVotes;
	}

	public void setYesVotes(int yesVotes) {
		this.yesVotes = yesVotes;
	}

	public int getNoVotes() {
		return noVotes;
	}

	public void setNoVotes(int noVotes) {
		this.noVotes = noVotes;
	}

	public int getNotVoted() {
		return notVoted;
	}

	public void setNotVoted(int notVoted) {
		this.notVoted = notVoted;
	}
}
