package co.xarx.trix.api;

import java.io.Serializable;

public class NotificationView implements Serializable {
	private static final long serialVersionUID = -8515832598761372174L;

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