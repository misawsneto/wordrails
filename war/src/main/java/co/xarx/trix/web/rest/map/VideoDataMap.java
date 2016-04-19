package co.xarx.trix.web.rest.map;

import co.xarx.trix.api.v2.PageData;
import co.xarx.trix.api.v2.VideoData;
import co.xarx.trix.domain.Video;
import co.xarx.trix.domain.page.Page;
import org.modelmapper.PropertyMap;

public class VideoDataMap extends PropertyMap<Video, VideoData> {

	@Override
	protected void configure() {
		map(source.getUrl(), destination.getUrl());
	}
}
