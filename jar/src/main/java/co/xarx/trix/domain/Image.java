package co.xarx.trix.domain;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "originalHash"}))
public class Image extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6607038985063216969L;

	public static final String SIZE_SMALL = "small";
	public static final String SIZE_MEDIUM = "medium";
	public static final String SIZE_LARGE = "large";
	public static final String SIZE_ORIGINAL = "original";

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

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
		SPLASH(Size.MEDIUM),
		LOGIN(Size.MEDIUM),
		POST(Size.MEDIUM, Size.LARGE),
		COVER(Size.MEDIUM, Size.LARGE),
		PROFILE_PICTURE(Size.SMALL, Size.MEDIUM);

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

	protected Image() {
		this.pictures = new HashSet<>();
		this.hashs = new HashMap<>();
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
	private Set<Size> sizes;

	public Set<String> getSizeTags() {
		return sizes.stream().map(Size::toString).collect(Collectors.toSet());
	}

	public Set<Size> getSizes() {
		return sizes;
	}

	public Set<Size> getQualitySizes() {
		return sizes.stream().filter(size -> size.quality != null).collect(Collectors.toSet());
	}

	public Set<Size> getAbsoluteSizes() {
		return sizes.stream().filter(size -> size.xy != null).collect(Collectors.toSet());
	}

	@javax.validation.constraints.Size(min=1, max=100)
	public String title;
	
	@Lob
	@Deprecated
	public String caption;
	
	@Lob
	public String credits;

	public String originalHash;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "image_hash",
			joinColumns = @JoinColumn(name = "image_id"),
			uniqueConstraints = @UniqueConstraint(columnNames = {"hash", "sizeTag", "image_id"}))
	@MapKeyColumn(name = "sizeTag", nullable = false)
	@Column(name = "hash", nullable = false)
	public Map<String, String> hashs;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;

	public String get(Object key) {
		return hashs.get(key);
	}

	@ManyToMany
	@JoinTable(name = "image_picture", joinColumns = @JoinColumn(name = "image_id"))
	public Set<Picture> pictures;

	@PrePersist
	public void create(){
		createOrUpdate();
	}

	@PreUpdate
	public void update(){
		createOrUpdate();
	}

	private void createOrUpdate() {
		for(Picture pic : pictures) {
			hashs.put(pic.sizeTag, pic.file.hash);
		}
	}
}