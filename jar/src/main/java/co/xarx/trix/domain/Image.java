package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


@lombok.Getter @lombok.Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "originalHash"}))
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6607038985063216969L;

	public static final String SIZE_SMALL = "small";
	public static final String SIZE_MEDIUM = "medium";
	public static final String SIZE_LARGE = "large";
	public static final String SIZE_ORIGINAL = "original";

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public enum Size {

		FAVICON(new Integer[]{32, 32}),
		SMALL(100),
		MEDIUM(500),
		LARGE(1024);

		public Integer[] xy; //height & width
		public Integer quality; //height * width

		Size(Integer quality) {
			this.quality = quality;
		}

		Size(Integer[] size) {
			this.xy = size;
		}

		public static Size findByAbbr(String abbr) {
			for (Size v : values()) {
				if (v.toString().equalsIgnoreCase(abbr)) {
					return v;
				}
			}

			throw new InvalidArgumentException("Size does not exist for value " + abbr);
		}

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	public enum Type {

		FAVICON(Size.FAVICON),
		SPLASH(Size.MEDIUM,Size.LARGE),
		LOGIN(Size.MEDIUM),
		POST(Size.MEDIUM, Size.LARGE),
		COVER(Size.MEDIUM, Size.LARGE),
		PROFILE_PICTURE(Size.SMALL, Size.MEDIUM),
		CATEGORY(Size.MEDIUM, Size.LARGE),
		VIDEO(Size.MEDIUM, Size.LARGE),
		AUDIO(Size.MEDIUM, Size.LARGE),
		LOGO(Size.SMALL, Size.MEDIUM);

		private Set<Size> sizes; //height & width

		Type(Size... sizes) {
			this.sizes = Sets.newHashSet(sizes);
		}

		private static Type findByAbbr(String abbr){
			for(Type v : values()){
				if(v.toString().equalsIgnoreCase(abbr)){
					return v;
				}
			}

			throw new InvalidArgumentException("Type does not exist for value " + abbr);
		}
	}

	@Lob
	public String externalImageUrl;

	protected Image() {
		this.sizes = new HashSet<>();
		this.pictures = new HashSet<>();
		this.hashes = new HashMap<>();
	}

	public Image(Type type) {
		this();
		this.sizes = type.sizes;
	}

	public Image(String type) {
		this();
		Type t = Type.findByAbbr(type);
		this.sizes = t.sizes;
	}

	@Transient
	@Setter(AccessLevel.NONE)
	private Set<Size> sizes;

	@JsonIgnore
	public Set<String> getSizeTags() {
		return sizes.stream().map(Size::toString).collect(Collectors.toSet());
	}

	public Set<Size> getSizes() {
		return sizes;
	}

	@JsonIgnore
	public Set<Size> getQualitySizes() {
		return sizes.stream().filter(size -> size.quality != null).collect(Collectors.toSet());
	}

	@JsonIgnore
	public Set<Size> getAbsoluteSizes() {
		return sizes.stream().filter(size -> size.xy != null).collect(Collectors.toSet());
	}

	@javax.validation.constraints.Size(min=1, max=100)
	private String title;
	
	@Lob
	@Deprecated
	private String caption;
	
	@Lob
	private String credits;

	private String originalHash;

	private String originalUrl;

	@SdkInclude
	public String getImageUrl(){
		return null;
	}

	@SdkInclude
	private String getImageSmallUrl(){
		return null;
	}
	@SdkInclude
	private String getMeidumUrl(){
		return  null;
	};
	@SdkInclude
	private String getLargeUrl(){
		return null;
	};


	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "image_hash",
			joinColumns = @JoinColumn(name = "image_id"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "sizeTag", "image_id"}))
	@MapKeyColumn(name = "sizeTag", nullable = false)
	@Column(name = "hash", nullable = false)
	private Map<String, String> hashes;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	private boolean vertical = false;

	@SdkExclude
	@ManyToMany
	@JoinTable(name = "image_picture", joinColumns = @JoinColumn(name = "image_id"))
	@JsonIgnore
	private Set<Picture> pictures;

	private Set<String> getPicturesSizes() {
		return getPictures().stream().map(Picture::getSizeTag).collect(Collectors.toSet());
	}

	public void setPictures(Set<Picture> pictures) {
		pictures.forEach(this::addPicture);
	}

	public boolean addPicture(Picture picture) {
		if(getPicturesSizes().contains(picture.getSizeTag())) {
			return false;
		}

		getPictures().add(picture);

		if(!Objects.equals(picture.getSizeTag(), "original"))
			sizes.add(Size.findByAbbr(picture.getSizeTag()));

		return true;
	}

	@PrePersist
	public void create(){
		createOrUpdate();
	}

	@PreUpdate
	public void update(){
		createOrUpdate();
	}

	private void createOrUpdate() {
		for(Picture pic : getPictures()) {
			getHashes().put(pic.sizeTag, pic.file.hash);
		}
	}

	@SdkInclude
	public String getOriginalHash() {
		return originalHash;
	}

	@Deprecated
	@SdkInclude
	public String getLargeHash() {
		return getHashes().get(SIZE_LARGE);
	}

	@Deprecated
	@SdkInclude
	public String getMediumHash() {
		return getHashes().get(SIZE_MEDIUM);
	}

	@Deprecated
	@SdkInclude
	public String getSmallHash() {
		return getHashes().get(SIZE_SMALL);
	}
}