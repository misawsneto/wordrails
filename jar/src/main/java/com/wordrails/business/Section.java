package com.wordrails.business;

import javax.persistence.*;

/**
 * Created by misael on 28/09/2015.
 */
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String name;

    @Lob
    public String content;

    @ManyToOne
    @JoinColumn(name = "network_id")
    public Network network;
}
