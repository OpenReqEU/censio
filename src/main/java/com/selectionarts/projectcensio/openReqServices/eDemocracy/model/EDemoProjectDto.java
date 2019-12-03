package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

import java.util.ArrayList;
import java.util.List;

public class EDemoProjectDto {

	private int id;
	private String title;
    private String phase_end;
    private String phase_candidates;
    private List<SimpleTicket> tickets;
    
    public EDemoProjectDto() {
		// TODO Auto-generated constructor stub
	}
    
    public EDemoProjectDto(int id, String title, String phase_end, String phase_candidates, Ticket[] tickets) {
		super();
		this.id = id;
		this.title = title;
		this.phase_end = phase_end;
		this.phase_candidates = phase_candidates;
		
		SimpleTicket yesTicket= new SimpleTicket("yes");
		SimpleTicket noTicket= new SimpleTicket("no");
		this.tickets = new ArrayList<SimpleTicket>();
		this.tickets.add(yesTicket);
		this.tickets.add(noTicket);
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
	public String getPhase_end() {
		return phase_end;
	}
	public void setPhase_end(String phase_end) {
		this.phase_end = phase_end;
	}
	public String getPhase_candidates() {
		return phase_candidates;
	}
	public void setPhase_candidates(String phase_candidates) {
		this.phase_candidates = phase_candidates;
	}

	public List<SimpleTicket> getTickets() {
		return tickets;
	}

	public void setTickets(List<SimpleTicket> tickets) {
		this.tickets = tickets;
	}
	
    
}
