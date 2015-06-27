package com.wordrails.business;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed
@Entity
@DiscriminatorValue(value = "SCHEDULED")
public class PostScheduled extends Post {
}
