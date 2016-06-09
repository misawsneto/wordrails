package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.EqualsAndHashCode(callSuper = false)
@Entity
public class Video extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6417000117605453415L;

	public static class VideoExternalProvider {
		public static Map<String, String> providers;
		static {
			providers = new HashMap<>();
			providers.put("DAILYMOTION", "http://www.dailymotion.com/video/");
			providers.put("YOUTUBE", "http://www.youtube.com/watch?v=");
			providers.put("VIMEO", "http://vimeo.com");
		}
	}

	public static final String INTERNAL_VIDEO = "internal";
	public static final String EXTERNAL_VIDEO = "external";

	public Video(File file) {
		file.original = true;
		this.file = file;
	}

	public Video(String identifier, String provider) {
		this.identifier = identifier;
		this.provider = provider;
	}

	public String identifier;
	public String provider;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String title;

	@ManyToOne(cascade=CascadeType.MERGE)
	@SdkExclude
	public File file;

	@SdkInclude
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Image image;

	@SdkInclude
	public String getImageHash() {
		if (image != null) return image.getOriginalHash();

		return null;
	}

	@SdkInclude
	public String getType(){
		if(file == null){
			return EXTERNAL_VIDEO;
		}
		return INTERNAL_VIDEO;
	}

	@SdkInclude
	public String getExternalVideoUrl(){
		if(getType().equals(EXTERNAL_VIDEO) && provider != null && identifier != null){
			return VideoExternalProvider.providers.get(getProvider()) +  identifier;
		} return null;
	}

	public String url;
}
