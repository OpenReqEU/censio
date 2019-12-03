package com.selectionarts.projectcensio.openReqServices.eDemocracy.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.selectionarts.projectcensio.model.Task;

@Data
@Entity
public class EdemocracyVoting {

    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private long id;

    private int eDemoProjectId;
    
    private int yesTicketId;
    private int noTicketId;
    
    @Column(unique = true, updatable = false, nullable = false)
    private String publicUuid;
    
    @OneToOne
    private Task task;
    
    @OneToMany
    private List<EdemocracyParticipation> participations;
    
    public EdemocracyVoting() {
    	this.publicUuid = UUID.randomUUID().toString();
    }
}

