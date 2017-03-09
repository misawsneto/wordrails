package co.xarx.trix.domain;

public enum NotificationType {
	//POST
	POST_ADDED,
	POST_COMMENTED,

	//COMMENT
	COMMENT_ADDED,

	//ADMIN
	ROLE_UPDATE,
	POST_TO_REVIEW,

	//SOCIAL
	NEW_FOLLOWER,

	//SYSTEM
	UPDATE_APP,
	GENERIC_NOTIFICATION
}
