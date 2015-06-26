package com.wordrails.business;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="DRAFT")
public class PostDraft extends Post {
}
