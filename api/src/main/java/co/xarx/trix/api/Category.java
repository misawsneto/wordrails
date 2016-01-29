package co.xarx.trix.api;

import java.io.Serializable;

public class Category implements Serializable {

	private static final long serialVersionUID = -2271798679380862532L;

	public Category() {
	}

	public Category(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer id;
	public String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}