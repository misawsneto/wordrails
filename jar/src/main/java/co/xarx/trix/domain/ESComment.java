package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Getter
@Setter
@Document(indexName = "#{properties.index}", type = Constants.ObjectType.COMMENT)
public class ESComment implements ElasticSearchEntity {

	private static final long serialVersionUID = 5686194740310479290L;

	@Id
	private Integer id;

	private String body;
	private Date date;

	private Integer postId;
	private Integer authorId;
	private Integer stationId;

	public String tenantId;

	@Override
	public String getType() {
		return Constants.ObjectType.COMMENT;
	}
}
