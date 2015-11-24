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
	public static class Layout {
		public static final String VERTICAL_LIST = "vertical_list";
	}

	public static class ElasticSearch {
		public static final String TYPE_POST = "post";
		public static final String TYPE_BOOKMARK = "bookmark";
	}
}
