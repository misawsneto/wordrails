package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentData implements Serializable, Identifiable {

	@Id
	private Integer id;

	private String body;
	private Date date;

	private Integer postId;
	private Integer authorId;
	private Integer stationId;
	public int commentsCount;

	private PersonData author;
}