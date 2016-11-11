package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Document(indexName = "#{properties.access_index}", type = Constants.ObjectType.ANALYTICS_INDEX_TYPE)
public class ESstatEvent implements ElasticSearchEntity {

    public ESstatEvent(){
        this.id = new BigInteger(UUID.randomUUID().toString().getBytes()).intValue();
        this.terms = new ArrayList<>();
    }

    @Id
    public Integer id;
    public String message;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    public Date timestamp;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String host;

    public String type;

    public String action;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String clientip;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String referrer;

    public Integer postId;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String postSlug;

    public Integer timeReading;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String browser;

    public Integer personId;

    public Integer authorId;

    public Integer stationId;

    public List<Integer> terms;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String tenantId;

//            "geoip": { "ip": "187.113.123.216","country_code2": "BR","country_code3": "BRA","country_name": "Brazil","continent_code": "SA","region_name": "30","city_name": "Recife","latitude": -8.050000000000011,"longitude": -34.900000000000006,"timezone": "America/Recife","real_region_name": "Pernambuco","location": [ -34.900000000000006,-8.050000000000011]},

}
