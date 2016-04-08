package co.xarx.trix.api.hal;

import lombok.Data;

@Data
public class PageHal extends HalResource {

	private String title;
	private HalResources sections;
}
