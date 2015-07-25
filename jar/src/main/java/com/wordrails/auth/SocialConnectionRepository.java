package com.wordrails.auth;

import com.wordrails.business.User;
import com.wordrails.business.UserConnection;
import com.wordrails.persistence.UserConnectionRepository;
import com.wordrails.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

@Component
public class SocialConnectionRepository implements ConnectionRepository {

	private Integer userId;
	private Integer networkId;

	@Autowired
	private UserRepository userRepository;
	@Qualifier("userConnectionRepository")
	@Autowired
	private UserConnectionRepository userConnectionRepository;

	private ConnectionFactoryLocator connectionFactoryLocator;
	private TextEncryptor textEncryptor;

	public SocialConnectionRepository() {
	}

	public SocialConnectionRepository(String userId, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
		String[] data = userId.split("_");
		this.userId = Integer.valueOf(data[0]);
		this.networkId = Integer.valueOf(data[1]);
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.textEncryptor = textEncryptor;
	}

	private ConnectionData toConnectionData(UserConnection socialUser) {
		return new ConnectionData(
				socialUser.providerId,
				socialUser.providerUserId,
				socialUser.displayName,
				socialUser.profileUrl,
				socialUser.imageUrl,

				decrypt(socialUser.accessToken),
				decrypt(socialUser.secret),
				decrypt(socialUser.refreshToken),

				convertZeroToNull(socialUser.expireTime)
		);
	}

	private Long convertZeroToNull(Long expireTime) {
		return (expireTime != null && expireTime == 0 ? null : expireTime);
	}

	private String decrypt(String encryptedText) {
		return (textEncryptor != null && encryptedText != null) ? textEncryptor.decrypt(encryptedText) : encryptedText;
	}

	private String encrypt(String text) {
		return (textEncryptor != null && text != null) ? textEncryptor.encrypt(text) : text;
	}

	private Connection<?> createConnection(ConnectionData connectionData) {
		ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
		return connectionFactory.createConnection(connectionData);
	}

	@Override
	public MultiValueMap<String, Connection<?>> findAllConnections() {
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<>();

		List<UserConnection> userConnections = userConnectionRepository.findAll();
		for (UserConnection userConnection : userConnections) {
			ConnectionData connectionData = toConnectionData(userConnection);
			Connection<?> connection = createConnection(connectionData);
			connections.add(connectionData.getProviderId(), connection);
		}

		return connections;
	}

	@Override
	public List<Connection<?>> findConnections(String providerId) {
		List<Connection<?>> connections = new ArrayList<>();

		List<UserConnection> userConnections = userConnectionRepository.findByProviderId(providerId, networkId);
		for (UserConnection userConnection : userConnections) {
			ConnectionData connectionData = toConnectionData(userConnection);
			Connection<?> connection = createConnection(connectionData);
			connections.add(connection);
		}

		return connections;
	}

	@Override
	public <A> List<Connection<A>> findConnections(Class<A> apiType) {
		String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();

		// do some lame stuff to make the casting possible
		List<?> connections = findConnections(providerId);
		return (List<Connection<A>>) connections;
	}

	@Override
	public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUserIds) {
		MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<>();

		List<String> fbIds = providerUserIds.get("facebook");
		List<String> ttIds = providerUserIds.get("twitter");
		List<String> ggIds = providerUserIds.get("google");

		List<UserConnection> userConnections = userConnectionRepository.findByProvidersAndUserId(fbIds, ttIds, ggIds, networkId);
		for (UserConnection userConnection : userConnections) {
			ConnectionData connectionData = toConnectionData(userConnection);
			Connection<?> connection = createConnection(connectionData);
			connections.add(connectionData.getProviderId(), connection);
		}

		return connections;
	}

	@Override
	public Connection<?> getConnection(ConnectionKey connectionKey) {
		UserConnection userConnection = userConnectionRepository.
				findByProviderIdAndProviderUserId(connectionKey.getProviderId(), connectionKey.getProviderUserId(), networkId);

		if(userConnection == null) {
			throw new NoSuchConnectionException(new ConnectionKey(connectionKey.getProviderId(), connectionKey.getProviderUserId()));
		}

		return createConnection(toConnectionData(userConnection));
	}

	@Override
	public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
		String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();

		return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
	}

	@Override
	public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
		Connection<A> connection = findPrimaryConnection(apiType);
		if (connection == null) {
			String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
			throw new NotConnectedException(providerId);
		}
		return connection;
	}

	@Override
	public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
		String providerId = connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();

		return getConnection(apiType, providerId);
	}

	@Override
	public void addConnection(Connection<?> connection) {
		ConnectionData connectionData = connection.createData();

		// check if this social account is already connected to a local account
		List<UserConnection> userConnections = userConnectionRepository.findByProviderId(connectionData.getProviderId(), networkId);

		if (userConnections != null) {
			for(UserConnection userConnection : userConnections) {
				if(userConnection.providerId.equals(connectionData.getProviderId()))
					if(userConnection.providerUserId.equals(connectionData.getProviderUserId()))
						throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
			}
		}

		User user = userRepository.findOne(userId);

		if(user == null) {
			throw new NotConnectedException("user " + userId + " does not exist");
		}

		UserConnection userConnection = new UserConnection();

		userConnection.user = user;
		userConnection.providerId = connectionData.getProviderId();
		userConnection.providerUserId = connectionData.getProviderUserId();
		userConnection.displayName = connectionData.getDisplayName();
		userConnection.profileUrl = connectionData.getProfileUrl();
		userConnection.imageUrl = connectionData.getImageUrl();

		// encrypt these values
		userConnection.accessToken = encrypt(connectionData.getAccessToken());
		userConnection.secret = encrypt(connectionData.getSecret());
		userConnection.refreshToken = encrypt(connectionData.getRefreshToken());

		userConnection.expireTime = connectionData.getExpireTime();

		try {
			userConnectionRepository.save(userConnection);
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateConnectionException(new ConnectionKey(connectionData.getProviderId(), connectionData.getProviderUserId()));
		}
	}

	@Override
	public void updateConnection(Connection<?> connection) {
		ConnectionData connectionData = connection.createData();
		UserConnection userConnection = userConnectionRepository.
				findByProviderIdAndProviderUserId(connectionData.getProviderId(), connectionData.getProviderUserId(), networkId);
		if (userConnection != null) {
			userConnection.displayName = connectionData.getDisplayName();
			userConnection.profileUrl = connectionData.getProfileUrl();
			userConnection.imageUrl = connectionData.getImageUrl();

			userConnection.accessToken = encrypt(connectionData.getAccessToken());
			userConnection.secret = encrypt(connectionData.getSecret());
			userConnection.refreshToken = encrypt(connectionData.getRefreshToken());

			userConnection.expireTime = connectionData.getExpireTime();
			userConnectionRepository.save(userConnection);
		}
	}

	@Override
	public void removeConnections(String providerId) {
		userConnectionRepository.deleteByProviderId(providerId, networkId);
	}

	@Override
	public void removeConnection(ConnectionKey connectionKey) {
		userConnectionRepository.deleteByProviderIdAndProviderUserId(connectionKey.getProviderId(), connectionKey.getProviderUserId(), networkId);
	}
}
