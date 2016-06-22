package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Document(indexName = "#{properties.index}", type = Constants.ObjectType.POST)
public class ESPost implements ElasticSearchEntity {

	private static final long serialVersionUID = 5686194740310479290L;

	public ESPost() {
		this.scheduledDate = new Date(0);
	}

	@Id
	public Integer id;
	public String tenantId;

	public Integer authorId;

	@JsonIgnore
	@Field(store = false, type = FieldType.Object)
	public ESPerson author;

	public Integer stationId;
	public String stationName;

	public String title;

	public Set<Integer> categories;
	public Set<String> tags;
	public Date date;
	public String body;
	public String topper;
	public String state;

	public int bookmarksCount;
	public int recommendsCount;
	public int commentsCount;

	@Field(index = FieldIndex.not_analyzed, type = FieldType.String)
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
