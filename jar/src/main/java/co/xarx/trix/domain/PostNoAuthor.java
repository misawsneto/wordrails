package co.xarx.trix.domain;

//import org.hibernate.search.annotations.Indexed;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
//@Indexed
@DiscriminatorValue(value="NOAUTHOR")
public class PostNoAuthor extends Post {

    public PostNoAuthor() {
        state = Post.STATE_NO_AUTHOR;
    }
}