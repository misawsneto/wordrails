package com.wordrails.business;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by misael on 28/09/2015.
 */
@Entity
public class Section implements Serializable {

    private static final long serialVersionUID = 7424825842348684233L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String name;

    @Lob
    public String loggedInUrl;

    @Lob
    public String anonymousUrl;

    @Lob
    public String content;

    @ManyToOne
    @JoinColumn(name = "network_id")
    public Network network;
}
