package co.xarx.trix.persistence.elasticsearch;

import co.xarx.trix.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import co.xarx.trix.domain.Person;

/**
 * Created by jonas on 08/10/15.
 */
public class BookmarkIndexed {
	public Integer id;
	@JsonManagedReference
	public Post post;
	@JsonManagedReference
	public Person person;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	public java.util.Date createdAt;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	public java.util.Date updatedAt;
}