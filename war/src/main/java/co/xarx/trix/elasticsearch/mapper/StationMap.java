package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.ESStation;
import org.modelmapper.PropertyMap;

public class StationMap extends PropertyMap<Station, ESStation> {

	@Override
	protected void configure() {
		map().setLogo(source.logo.getHashes());
		map().setTenantId(source.getTenantId());
	}
}
