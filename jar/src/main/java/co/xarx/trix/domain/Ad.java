package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
public class Ad extends BaseEntity {

	@OneToOne
	public Image image;
	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	@Lob
	public String link;

	@NotNull
	@ManyToOne
	Sponsor sponsor;

	@PrePersist
	public void onCreate() {
		if (image != null && image.original != null) {
			imageId = image.original.id;
			imageSmallId = image.small.id;
			imageMediumId = image.medium.id;
			imageLargeId = image.large.id;
		} else {
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}
	}

	@PreUpdate
	public void onUpdate() {
		if (image != null && image.original != null) {
			imageId = image.original.id;
			imageSmallId = image.small.id;
			imageMediumId = image.medium.id;
			imageLargeId = image.large.id;
		} else {
			imageId = null;
			imageSmallId = null;
			imageMediumId = null;
			imageLargeId = null;
		}
	}
}
