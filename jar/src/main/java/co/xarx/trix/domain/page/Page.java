package co.xarx.trix.domain.page;

import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.domain.Station;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@lombok.Getter @lombok.Setter
@Entity
@Table(name = "page")
@JsonIgnoreProperties({"updatedAt", "createdAt"})
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Page extends BaseEntity {

	public String title;

	public Page(){}

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "page_section",
			joinColumns = @JoinColumn(name = "page_id"),
			inverseJoinColumns = @JoinColumn(name = "section_id"))
	@MapKeyJoinColumn(name = "list_index")
	@JsonProperty("sections")
	public Map<Integer, AbstractSection> sections;

	@JsonProperty("createdAt")
	public Date getCreatedAt(){
		return this.createdAt;
	}

	@ManyToOne
	@JsonBackReference("station")
	public Station station;

}
