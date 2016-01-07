package co.xarx.trix.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"type", "tenantId", "originalHash"}))
public class Image extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -6607038985063216969L;

	public static final String SIZE_SMALL = "small";
	public static final String SIZE_MEDIUM = "medium";
	public static final String SIZE_LARGE = "large";
	public static final String SIZE_ORIGINAL = "original";

	public enum Type {
		FAVICON(0, new HashMap<String, Integer[]>() {{put(SIZE_SMALL, new Integer[]{32,32});}}),
		SPLASH(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 500);}}),
		LOGIN(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 500);}}),
		POST(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 400);put(SIZE_LARGE, 1024);}}),
		COVER(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 400);put(SIZE_LARGE, 1024);}}),
		PROFILE_PICTURE(new HashMap<String, Integer>() {{put(SIZE_SMALL, 100);put(SIZE_MEDIUM, 500);}});

		public Map<String, Integer[]> sizes; //height & width
		public Map<String, Integer> qualities; //height * width

		Type(Map<String, Integer> qualities) {
			this.qualities = qualities;
		}

		//first integer is just to create this second constructor. its not elegant, but we can't do much with enum
		Type(Integer x, Map<String, Integer[]> sizes) {
			this.sizes = sizes;
		}

		public Set<String> getSizeTags() {
			if(qualities != null) return qualities.keySet();
			else return sizes.keySet();
		}

		public static Type findByAbbr(String abbr){
			for(Type v : values()){
				if( v.toString().equals(abbr)){
					return v;
				}
			}
			return POST;
		}

		public Integer count() {
			if(sizes != null) return sizes.size();
			else return qualities.size();
		}
	}

	public Image() {
		this.pictures = new HashSet<>();
		this.hashs = new HashMap<>();
	}

	@Size(min=1, max=100)
	public String title;
	
	@Lob
	@Deprecated
	public String caption;
	
	@Lob
	public String credits;

	@Column(columnDefinition = "varchar(255) default 'POST'", nullable = false)
	public String type;

	@ManyToMany
	@JoinTable(name = "image_picture", joinColumns = @JoinColumn(name = "image_id"))
	public Set<Picture> pictures;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name = "image_hash", joinColumns = @JoinColumn(name = "image_id"))
	@MapKeyColumn(name = "sizeTag", nullable = false)
	@Column(name = "hash", nullable = false)
	public Map<String, String> hashs;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;

	public String get(Object key) {
		return hashs.get(key);
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
		for(Picture pic : pictures) {
			hashs.put(pic.sizeTag, pic.file.hash);
		}
	}
}