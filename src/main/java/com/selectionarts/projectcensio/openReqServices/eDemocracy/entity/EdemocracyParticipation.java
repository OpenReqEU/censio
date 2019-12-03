package com.selectionarts.projectcensio.openReqServices.eDemocracy.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class EdemocracyParticipation {

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;
    
	private String username;
	private int usernameId;
	
	@Column(unique = true, updatable = false, nullable = false)
	private String uuid;
	private int voting;
	
	public EdemocracyParticipation() {
		// TODO Auto-generated constructor stub
	}
	
	public EdemocracyParticipation(String username, int usernameId, String uuid) {
		super();
		this.username = username;
		this.usernameId = usernameId;
		this.uuid = uuid;
	}
}

