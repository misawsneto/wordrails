package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StationData implements Serializable, Identifiable {

	private Integer id;
	private String name;

	public String slug;
	public ImageData logo;

	public boolean main;
	public boolean writable;

	public boolean readableForAnonymous;
	public boolean allowComments;
	public boolean allowSocialShare;

	public boolean allowWritersToNotify;
	public boolean topper;

	public boolean subheading;
	public boolean showAuthorData;

	public boolean showAuthorSocialData;
}