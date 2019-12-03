package com.selectionarts.projectcensio.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Types {

    @Id
    @GeneratedValue
    @Column(unique = true, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;


    private String type;
    private String title;

    public Types() {
    }

    public Types(String type, String title) {
        this.type = type;
        this.title = title;
    }

    @Override
    public boolean equals(Object o)
    {
        return this.getId() == ((Types) o).getId();
    }
}
