package co.xarx.trix.util;

import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserGrantedAuthority;

public final class Constants {


	private Constants() {
	}


	public static class Authentication {
		public static final User ANONYMOUS_USER = new User();

		static {
			ANONYMOUS_USER.username = "wordrails";
			ANONYMOUS_USER.addAuthority(new UserGrantedAuthority(ANONYMOUS_USER, "ROLE_ANONYMOUS"));
		}

	}

	public static class Section {
		public static final String QUERYABLE_LIST = "queryable";
		public static final String CONTAINER = "container";
		public static final String LINK = "link";
	}

	public static class Layout {
		public static final String SECTION_HORIZONTAL_ORIENTATION = "horizontal_orientation";
		public static final String SECTION_VERTICAL_ORIENTATION = "vertical_orientation";

		public static final String SECTION_VERTICAL_LIST = "vertical_list";

		public static final String BLOCK_UNDER_IMAGE = "under_image";
		public static final String BLOCK_OVER_IMAGE = "over_image";
	}

	public static class ObjectType {
		public static final String POST = "post";
		public static final String PERSON = "person";
		public static final String STATION = "station";
		public static final String COMMENT = "comment";
		public static final String APP_STATS = "app_stats";
	}

	public static class Post {
		public static final String STATE_DRAFT = "DRAFT";
		public static final String STATE_NO_AUTHOR = "NOAUTHOR";
		public static final String STATE_TRASH = "TRASH";
		public static final String STATE_PUBLISHED = "PUBLISHED";
		public static final String STATE_UNPUBLISHED = "UNPUBLISHED";
		public static final String STATE_SCHEDULED = "SCHEDULED";
	}

	public enum MobilePlatform {
		APPLE,
		ANDROID
	}

	public static class StatsEventType {
		public static final String POST_READ = "post_read";
		public static final String POST_COMMENT = "post_comment";
		public static final String POST_BOOKMARK = "post_bookmark";
		public static final String POST_RECOMMEND = "post_recommend";
	}

	public static class AnalyticsType {
		public static final String READ = "nginx_access";
	}
}
