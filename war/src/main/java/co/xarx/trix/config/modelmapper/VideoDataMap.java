package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.VideoData;
import co.xarx.trix.domain.Video;
import org.modelmapper.PropertyMap;

public class VideoDataMap extends PropertyMap<Video, VideoData> {

	@Override
	protected void configure() {
		map(source.getUrl(), destination.getUrl());
	}
}
