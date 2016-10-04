package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@lombok.Getter @lombok.Setter @lombok.NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
	public class PostByTerm{
		public List<PostView> posts;
	}