package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Getter
@Setter
@Document(indexName = "#{properties.index}", type = Constants.ObjectType.POSTREAD)
public class ESPostread implements ElasticSearchEntity {

    @Id
    public Integer id;
    public String message;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    public Date timestamp;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String host;

    public String type;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String clientip;

    public String verb;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String request;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String httpversion;

    public Integer response;

    public Integer bytes;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String referrer;

    public Integer postId;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String postSlug;

    public String action;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String os_name;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String device;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String browser;

    public Integer authorId;

    public Integer stationId;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    public String tenantId;

//            "geoip": { "ip": "187.113.123.216","country_code2": "BR","country_code3": "BRA","country_name": "Brazil","continent_code": "SA","region_name": "30","city_name": "Recife","latitude": -8.050000000000011,"longitude": -34.900000000000006,"timezone": "America/Recife","real_region_name": "Pernambuco","location": [ -34.900000000000006,-8.050000000000011]},

}
