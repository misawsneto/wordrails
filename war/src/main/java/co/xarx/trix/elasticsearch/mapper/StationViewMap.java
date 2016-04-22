package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.api.StationView;
import co.xarx.trix.domain.ESStation;
import co.xarx.trix.domain.Station;
import org.modelmapper.PropertyMap;

public class StationViewMap extends PropertyMap<Station, StationView> {

	@Override
	protected void configure() {
		map().setLogo(source.logo.getHashs());
	}
}
