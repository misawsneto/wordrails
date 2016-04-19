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
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostData implements Serializable, Identifiable {

	@Id
	private Integer id;

	private String title;
	private String snippet;
	private String body;
	private String subheading;
	private String topper;
	private String state;

	private Set<Integer> categoriesIds;
	private Set<CategoryData> categories;
	private Set<String> tags;

	private Integer authorId;
	private PersonData author;

	private Date date;

	private String imageHash;
	private String videoHash;
	private String audioHash;

	private PostImageData image;
	private VideoData video;
	private AudioData audio;

	private String slug;

	private Double lat;
	private Double lng;

	private Date scheduledDate;
	private boolean notified;
}