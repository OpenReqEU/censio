package com.selectionarts.projectcensio.openReqServices.eDemocracy.model;

import java.util.ArrayList;
import java.util.List;

public class EDemoReturnProjectDto {

	private int id;
	private String title;
    private String phase_end_at;
    private String phase_candidates_at;
    private List<Ticket> tickets;
    
    public EDemoReturnProjectDto() {
		// TODO Auto-generated constructor stub
	}
    
    public EDemoReturnProjectDto(int id, String title, String phase_end_at, String phase_candidates_at, Ticket[] tickets) {
		super();
		this.id = id;
		this.title = title;
		this.phase_end_at = phase_end_at;
		this.phase_candidates_at = phase_candidates_at;
		
		Ticket yesTicket= new Ticket(0,"yes","yes url",0,"I want the requirement to be implemented.");
		Ticket noTicket= new Ticket(0,"no","no url",0,"I don't want the requirement to be implemented.");
		this.tickets = new ArrayList<Ticket>();
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
	public String getPhase_end_at() {
		return phase_end_at;
	}
	public void setPhase_end_at(String phase_end) {
		this.phase_end_at = phase_end;
	}
	public String getPhase_candidates_at() {
		return phase_candidates_at;
	}
	public void setPhase_candidates_at(String phase_candidates) {
		this.phase_candidates_at = phase_candidates;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
    
}
