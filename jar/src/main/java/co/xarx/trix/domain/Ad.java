package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
public class Ad {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer id;

	@OneToOne
	public Image image;
	public Integer imageId;
	public Integer imageSmallId;
	public Integer imageMediumId;
	public Integer imageLargeId;

	@Lob
	public String link;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	public Date createdAt;

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

		createdAt = updatedAt = new Date();
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

		updatedAt = new Date();
	}
}
