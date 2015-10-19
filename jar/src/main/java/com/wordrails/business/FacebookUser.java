package com.wordrails.business;


public class FacebookUser {

	private String id;
	private String name;
	private String email;
	private Cover cover;

	public static class Cover {
		private String id;
		private int offset_y;
		private String source;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getOffset_y() {
			return offset_y;
		}

		public void setOffset_y(int offset_y) {
			this.offset_y = offset_y;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Cover getCover() {
		return cover;
	}

	public void setCover(Cover cover) {
		this.cover = cover;
	}
}
