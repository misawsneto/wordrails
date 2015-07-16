package com.wordrails.auth;

import com.wordrails.business.UserConnection;
import com.wordrails.persistence.UserConnectionRepository;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SocialUsersConnectionRepository implements UsersConnectionRepository {

	@Qualifier("userConnectionRepository")
	@Autowired
	private UserConnectionRepository userConnectionRepository;

	private TextEncryptor textEncryptor;

	private ConnectionFactoryLocator connectionFactoryLocator;

	public SocialUsersConnectionRepository() {
	}

	public SocialUsersConnectionRepository(TextEncryptor textEncryptor, ConnectionFactoryLocator connectionFactoryLocator) {
		this.textEncryptor = textEncryptor;
		this.connectionFactoryLocator = connectionFactoryLocator;
	}

	@Override
	public List<String> findUserIdsWithConnection(Connection<?> connection) {
		List<String> ids = new ArrayList<>();
		ConnectionKey key = connection.getKey();
		UserConnection userConnection = userConnectionRepository.
				findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());

		if(userConnection != null) {
			ids.add(userConnection.id + "");
		}

		return ids;
	}

	@Override
	public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
		Set<String> ids = new HashSet<>();
		List<UserConnection> userConnections = userConnectionRepository.
				findByProviderIdAndUserIds(providerId, providerUserIds);

		for(UserConnection userConnection : userConnections) {
			ids.add(userConnection.id + "");
		}

		return ids;
	}

	@Override
	public ConnectionRepository createConnectionRepository(String userId) {
		return new SocialConnectionRepository(Integer.valueOf(userId), connectionFactoryLocator, textEncryptor);
	}
}
