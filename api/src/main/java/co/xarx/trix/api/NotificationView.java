package co.xarx.trix.api;

import java.io.Serializable;

/**
 * @author misael
 * @see NotificationDto
 */
public class NotificationView implements Serializable {
	private static final long serialVersionUID = -8515832598761372174L;
	public int id;
	public boolean seen;
	public String type;
	public String message;
	public int personId;
	public String personName;
	public String hash;
	public int networkId;
	public String networkName;
	public int stationId;
	public String stationName;
	public int postId;
	public String postTitle;
	public String postSnippet;
	public String imageSmallId;
	public String imageMediumId;
	public PostView post;
}