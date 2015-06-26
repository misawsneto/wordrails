package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@DiscriminatorValue(value="SCHEDULED")
public class PostScheduled extends Post {

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date scheduledDate;
}
