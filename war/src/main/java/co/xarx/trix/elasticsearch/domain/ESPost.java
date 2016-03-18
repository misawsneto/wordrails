package co.xarx.trix.elasticsearch.domain;

import co.xarx.trix.api.Category;
import co.xarx.trix.domain.ElasticSearchEntity;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Document(indexName = "#{properties.index}", type = Constants.ObjectType.POST)
public class ESPost implements ElasticSearchEntity {

	private static final long serialVersionUID = 5686194740310479290L;

	@Id
	public Integer id;
	public String tenantId;
	public Integer networkId;

	public Integer authorId;

	@JsonIgnore
	@Field(store = false, type = FieldType.Object)
	public ESPerson author;

	public Integer stationId;
	public String stationName;

	public String title;

	public Set<Category> categories;
	public Set<String> tags;
	public Boolean sponsored;
	public Date date;
	public String body;
	public String topper;
	public String state;

	public int readsCount;
	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	public String slug;
	public Integer readTime;

	public String featuredImageHash;
	public boolean imageLandscape;
	public String featuredImageCaption;
	public String featuredImageCredits;
	public String featuredImageTitle;

	public String featuredVideoHash;
	public String featuredAudioHash;

	public Double lat;
	public Double lng;

	public String subheading;
	public Date scheduledDate;
	public boolean notify;

	@Override
	public String getType() {
		return Constants.ObjectType.POST;
	}
}
