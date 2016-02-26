package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Station;
import co.xarx.trix.elasticsearch.domain.ESStation;
import org.modelmapper.PropertyMap;

public class StationMap extends PropertyMap<Station, ESStation> {

	@Override
	protected void configure() {
		map().setLogo(source.logo.getHashs());
		map().setTenantId(source.getTenantId());
	}
}
