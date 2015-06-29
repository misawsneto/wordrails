package com.wordrails.business;

import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Indexed
@Entity
@DiscriminatorValue(value = "TRASH")
public class PostTrash extends Post {
}