package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by misael on 27/08/2015.
 */
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @OneToOne
    public Image image;
    public Integer imageId;
    public Integer imageSmallId;
    public Integer imageMediumId;
    public Integer imageLargeId;

    @NotNull
    @ManyToOne
    Sponsor sponsor;

    @Lob
    public String link;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    public Date createdAt;

    @PrePersist
    public void onCreate() {
        if (image != null && image.original != null) {
            imageId = image.original.id;
            imageSmallId = image.small.id;
            imageMediumId = image.medium.id;
            imageLargeId = image.large.id;
        } else {
            imageId = null;
            imageSmallId = null;
            imageMediumId = null;
            imageLargeId = null;
        }

        createdAt = updatedAt = new Date();
    }

    @PreUpdate
    public void onUpdate() {
        if (image != null && image.original != null) {
            imageId = image.original.id;
            imageSmallId = image.small.id;
            imageMediumId = image.medium.id;
            imageLargeId = image.large.id;
        } else {
            imageId = null;
            imageSmallId = null;
            imageMediumId = null;
            imageLargeId = null;
        }

        updatedAt = new Date();
    }
}
