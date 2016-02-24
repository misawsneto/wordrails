package co.xarx.trix.api;

import java.io.Serializable;

public class NotificationView implements Serializable {
	private static final long serialVersionUID = -8515832598761372174L;

	protected NotificationView() {
	}

	public NotificationView(String title, String message, String hash, boolean test) {
		this.title = title;
		this.message = message;
		this.hash = hash;
		this.test = test;
	}

	public int id;
	public String type;
	public String title;
	public String message;
	public String hash;
	public int networkId;
	public int postId;
	public String postTitle;
	public String postSnippet;
	public PostView post;
	public boolean test = true;
}