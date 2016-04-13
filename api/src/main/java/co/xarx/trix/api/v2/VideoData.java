package co.xarx.trix.api.v2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;


@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoData implements Serializable {

	private String url;
	private String credits;
	private String caption;
	private String title;
}