package co.xarx.trix.api;

import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.util.StringUtil;

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

	public static NotificationView of(PostView post) {
		String hash = StringUtil.generateRandomString(10, "Aa#");
		NotificationView notification = new NotificationView(post.title, post.title, hash, false);
		notification.type = MobileNotification.Type.POST_ADDED.toString();
		notification.post = post;
		notification.postId = (int) post.getId();
		notification.postTitle = post.title;
		notification.postSnippet = StringUtil.simpleSnippet(post.body);
		return notification;
	}

	public int id;
	public String type;
	public String title;
	public String message;
	public String hash;
	public int postId;
	public String postTitle;
	public String postSnippet;
	public PostView post;
	public boolean test = false;
}