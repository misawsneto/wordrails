package co.xarx.trix.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermView implements Serializable {
	private static final long serialVersionUID = 5504321793935627049L;

	public Integer termId;
	public String termName;
	public Integer parentId;

	public java.lang.Integer id;
	public java.lang.String color;
	public java.lang.String imageHash;
	public java.lang.String name;
	public java.lang.String description;
	public java.lang.Integer taxonomyId;
	public List<TermView> children;
}
