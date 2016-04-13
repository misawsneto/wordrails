package co.xarx.trix.api.v2;

import co.xarx.trix.api.Category;
import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
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

	private Set<Category> categories;
	private Set<String> tags;

	private PersonData author;

	private Date date;

	private ImageData image;
	private VideoData video;
	private String slug;

	private Double lat;
	private Double lng;

	private Date scheduledDate;
	private boolean notify;
}