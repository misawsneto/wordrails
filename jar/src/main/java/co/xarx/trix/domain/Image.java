package co.xarx.trix.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
public class Image extends BaseEntity implements Serializable {

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
	public String caption;
	
	@Lob
	public String credits;

	@Column(columnDefinition = "varchar(255) default 'POST'", nullable = false)
	public String type;

	@ManyToOne
	public Comment comment;

	@OneToOne(mappedBy="image")
	public Person person;

	@OneToOne(mappedBy="logo")
	public Network network;
	
	@OneToOne(mappedBy="logo")
	public Sponsor logoSponsor;

	@ManyToOne
	public Person owner;
	
	@ManyToOne
	public Station station;

	@OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
	public Set<Picture> pictures;

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name="image_hash", joinColumns=@JoinColumn(name="image_id"))
	@MapKeyColumn(name="sizeTag", nullable = false)
	@Column(name="hash", nullable = false)
	public Map<String, String> hashs;

	@Deprecated
	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File original;

	@Deprecated
	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File small;

	@Deprecated
	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File medium;

	@Deprecated
	@NotNull
	@ManyToOne(cascade=CascadeType.MERGE)
	public File large;

	@Deprecated
	public String originalHash;
	@Deprecated
	public String smallHash;
	@Deprecated
	public String mediumHash;
	@Deprecated
	public String largeHash;

	@ManyToOne
	public Post post;

	@OneToMany(mappedBy="featuredImage")
	public Set<Post> featuringPosts;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;
	
	public Integer postId;
	
	public Integer commentId;
	
	@PrePersist
	public void create(){
		createOrUpdate();
		setDeprecatedAttributes();
	}

	@PreUpdate
	public void update(){
		createOrUpdate();
	}

	private void createOrUpdate() {
		if (post != null) postId = post.id;
		if (comment != null) commentId = comment.id;

		for(Picture pic : pictures) {
			hashs.put(pic.sizeTag, pic.file.hash);
			switch (pic.sizeTag) {
				case SIZE_SMALL:
					smallHash = pic.file.hash;
					break;
				case SIZE_MEDIUM:
					mediumHash = pic.file.hash;
					break;
				case SIZE_LARGE:
					largeHash = pic.file.hash;
					break;
				case SIZE_ORIGINAL:
					originalHash = pic.file.hash;
					break;
			}
		}
	}

	private void setDeprecatedAttributes() {
		this.original = this.getPicture(Image.SIZE_ORIGINAL).file;
		Picture largePicture = this.getPicture(Image.SIZE_LARGE);
		Picture mediumPicture = this.getPicture(Image.SIZE_MEDIUM);
		Picture smallPicture = this.getPicture(Image.SIZE_SMALL);

		if(largePicture == null) {
			this.large = this.original;
		} else {
			this.large = largePicture.file;
		}

		if(mediumPicture == null) {
			this.medium = this.large;
		} else {
			this.medium = mediumPicture.file;
		}

		if(smallPicture == null) {
			this.small = this.medium;
		} else {
			this.small = smallPicture.file;
		}
	}

	public Picture getPicture(String sizeTag) {
		for(Picture pic : pictures) {
			if(pic.sizeTag.equals(sizeTag)) return pic;
		}

		return null;
	}
}