package com.wordrails.domain;

//import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@Indexed
@DiscriminatorValue(value = "TRASH")
public class PostTrash extends Post {

	public PostTrash() {
		state = Post.STATE_TRASH;
	}
}
