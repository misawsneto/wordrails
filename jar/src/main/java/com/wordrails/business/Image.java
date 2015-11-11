package com.wordrails.business;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@Entity
public class Image implements Serializable {

	public static final String SIZE_SMALL = "small";
	public static final String SIZE_MEDIUM = "medium";
	public static final String SIZE_LARGE = "large";
	public static final String SIZE_ORIGINAL = "original";

	public enum Type {
		FAVICON(0, new HashMap<String, Integer[]>() {{put(SIZE_SMALL, new Integer[]{32,32});}}),
		SPLASH(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 250000);}}),
		LOGIN(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 250000);}}),
		POST(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 90000);put(SIZE_LARGE, 655360);}}),
		COVER(new HashMap<String, Integer>() {{put(SIZE_MEDIUM, 90000);put(SIZE_LARGE, 655360);}}),
		PROFILE_PICTURE(new HashMap<String, Integer>() {{put(SIZE_SMALL, 10000);put(SIZE_MEDIUM, 250000);}});

		public Map<String, Integer[]> sizes; //height & width
		public Map<String, Integer> qualities; //height * width

		Type(Map<String, Integer> qualities) {
			this.qualities = qualities;
		}

		//first integer is just to create this second constructor. its not elegant, but we can't do much with enum
		Type(Integer x, Map<String, Integer[]> sizes) {
			this.sizes = sizes;
		}

		public static Type findByAbbr(String abbr){
			for(Type v : values()){
				if( v.toString().equals(abbr)){
					return v;
				}
			}
			return null;
		}
	}

	public Image() {
		this.pictures = new HashSet<>();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

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

	@OneToMany(mappedBy = "image")
	public Set<Picture> pictures;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL)
	public File original;

	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File small;

	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File medium;

	@NotNull
	@ManyToOne(cascade=CascadeType.REMOVE)
	public File large;

	public String originalHash;
	public String smallHash;
	public String mediumHash;
	public String largeHash;

	@ManyToOne
	public Post post;
	
	@OneToMany(mappedBy="featuredImage")
	public Set<Post> featuringPosts;
	
	@Column(columnDefinition = "boolean default false", nullable = false)
	public boolean vertical = false;
	
	public Integer postId;
	
	public Integer commentId;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;
	
	@PrePersist
	public void onCreate(){
		if (post != null) postId = post.id;

		if (comment != null) commentId = comment.id;

		smallHash = small.hash;
		mediumHash = medium.hash;
		largeHash = large.hash;
		originalHash = original.hash;

		createdAt = updatedAt = new Date();
	}
	
	@PreUpdate
	public void onUpdate(){
		if (post != null) postId = post.id;

		if (comment != null) commentId = comment.id;

		smallHash = small.hash;
		mediumHash = medium.hash;
		largeHash = large.hash;
		originalHash = original.hash;

		updatedAt = new Date();
	}

	public static boolean containsType(String string) {
		for (Image.Type type : Image.Type.values()) {
			if (type.name().equals(string)) {
				return true;
			}
		}
		return false;
	}

	public Picture getPicture(String sizeTag) {
		for(Picture pic : pictures) {
			if(pic.sizeTag.equals(sizeTag)) return pic;
		}

		return null;
	}
}