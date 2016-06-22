package co.xarx.trix.services.user;

import co.xarx.trix.domain.User;
import co.xarx.trix.domain.UserConnection;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.UserConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConnectionFactory {

	private UserConnectionRepository userConnectionRepository;

	@Autowired
	public UserConnectionFactory(UserConnectionRepository userConnectionRepository) {
		this.userConnectionRepository = userConnectionRepository;
	}

	public UserConnection create(User user, SocialUser socialUser) {
		UserConnection userConnection = new UserConnection();
		userConnection.setProviderId(socialUser.getProviderId());
		userConnection.setProviderUserId(socialUser.getId());
		userConnection.setEmail(socialUser.getEmail());
		userConnection.setDisplayName(socialUser.getName());
		userConnection.setProfileUrl(socialUser.getProfileUrl());
		userConnection.setImageUrl(socialUser.getProfileImageUrl());
		userConnection.setUser(user);
		userConnectionRepository.save(userConnection);
		return userConnection;
	}
}
