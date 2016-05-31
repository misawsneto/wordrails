package co.xarx.trix.services.security;

import co.xarx.trix.api.v2.AuthCredentialData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.persistence.AuthCredentialRepository;
import co.xarx.trix.persistence.NetworkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthCredentialsService {

	private NetworkRepository networkRepository;
	private AuthCredentialRepository authCredentialRepository;

	@Autowired
	public AuthCredentialsService(NetworkRepository networkRepository, AuthCredentialRepository authCredentialRepository) {
		this.networkRepository = networkRepository;
		this.authCredentialRepository = authCredentialRepository;
	}

	public void updateCredentials(AuthCredentialData dto) {
		String tenant = TenantContextHolder.getCurrentTenantId();
		AuthCredential auth = authCredentialRepository.findAuthCredentialByTenantId(tenant);

		if(auth == null) {
			auth = new AuthCredential();
			auth.setNetwork(networkRepository.findByTenantId(tenant));
		}

		auth.setFacebookAppID(dto.getFacebookAppID());
		auth.setFacebookAppSecret(dto.getFacebookAppSecret());
		auth.setGoogleAppID(dto.getGoogleAppID());
		auth.setGoogleAppSecret(dto.getGoogleAppSecret());

		authCredentialRepository.save(auth);
	}

	public AuthCredentialData getCredentials() {
		AuthCredential auth = authCredentialRepository.findAuthCredentialByTenantId(TenantContextHolder.getCurrentTenantId());

		return new AuthCredentialData(auth.getFacebookAppID(), auth.getFacebookAppSecret(), auth.getGoogleAppID(),
				auth.getGoogleAppSecret());
	}
}
