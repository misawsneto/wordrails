package co.xarx.trix.api.v2;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Taxonomy;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TermData{
	private static final long serialVersionUID = 7891255759575029731L;

	@Id
	public Integer id;

	public String name;

	public String name_parent;

	public Taxonomy taxonomy;

	@JsonBackReference("parent")
	public co.xarx.trix.domain.Term parent;

	public Set<co.xarx.trix.domain.Term> children;

	public Integer taxonomyId;
	public String taxonomyName;

	public String color;

	public Image image;

	public String imageUrl;

	public String description;
}