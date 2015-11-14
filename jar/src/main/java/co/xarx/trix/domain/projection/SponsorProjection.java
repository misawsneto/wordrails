package co.xarx.trix.domain.projection;

import java.util.Set;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Sponsor;
import org.springframework.data.rest.core.config.Projection;

@Projection(types=Sponsor.class)
public interface SponsorProjection {
	Integer getId();
	
	String getLogo();
	String getNetwork();
	Set<Image> getImages();
	String getName();
	String getKeywords();
}