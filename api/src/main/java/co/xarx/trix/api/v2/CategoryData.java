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
public class CategoryData implements Serializable, Identifiable {

	public CategoryData(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public Integer parentId;

	public java.lang.Integer id;
	public java.lang.String color;
	public java.lang.String imageHash;
	public java.lang.String name;
	public java.lang.String description;
	public java.lang.Integer taxonomyId;
}