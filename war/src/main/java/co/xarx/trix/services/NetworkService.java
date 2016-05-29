package co.xarx.trix.services;

import co.xarx.trix.api.AuthCredentialDto;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AuthCredential;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.AuthCredentialRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NetworkService {

	private Map<String, Integer> tenantIds; //key=tenantId, value=networkId
	private Map<String, Integer> domains; //key=domain, value=networkId

	private NetworkRepository networkRepository;
	private AuthCredentialRepository authCredentialRepository;

	@Autowired
	public NetworkService(NetworkRepository networkRepository, AuthCredentialRepository authCredentialRepository) {
		this.networkRepository = networkRepository;
		tenantIds = Maps.newConcurrentMap();
		domains = Maps.newConcurrentMap();

		List<Network> networks = networkRepository.findAll();
		for (Network n : networks) {
			tenantIds.put(n.tenantId, n.id);
			if (n.domain != null) domains.put(n.domain, n.id);
		}
		this.authCredentialRepository = authCredentialRepository;
	}

	public Network getNetworkFromHost(String host) {
		String tenantId = getTenantFromHost(host);

		if (tenantId.equals("settings")) { //gambiarra
			Network network = new Network();
			network.name = "Settings";
			network.tenantId = "settings";
			network.id = 0;
		}

		return networkRepository.findByTenantId(tenantId);
	}

	public String getTenantFromHost(String host) {
		String tenantId = StringUtil.getSubdomainFromHost(host);
		if("settings".equals(tenantId))
			return "settings";

		if (domains.keySet().contains(host)) {
			for (String s : tenantIds.keySet()) {
				if (Objects.equals(tenantIds.get(s), domains.get(host))) {
					tenantId = s;
					break;
				}
			}
		} else if (!tenantIds.keySet().contains(tenantId)) {
			Network network = networkRepository.findByDomain(host);
			if (network != null) {
				domains.put(host, network.id);
				tenantId = network.tenantId;
			} else {
				network = networkRepository.findByTenantId(tenantId);
				if (network != null) {
					tenantIds.put(tenantId, network.id);
				} else {
					return null;
				}
			}
		}

		return tenantId;
	}

	public String getNetworkInvitationTemplate() {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		try {
			String templateFile;
			templateFile = "complete-subscription-email.html";
			String filePath = new ClassPathResource(templateFile).getFile().getAbsolutePath();
			byte[] bytes = Files.readAllBytes(Paths.get(filePath));
			String template = new String(bytes, Charset.forName("UTF-8"));
			template = 	template.replaceAll("\\{\\{invitationTemplate}}", network.invitationMessage);
			return template;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public void updateAuthCredentials(AuthCredentialDto authCredential) {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		if(network.authCredential == null){
			network.authCredential = new AuthCredential();
			network.authCredential.setNetwork(network);
			authCredentialRepository.save(network.authCredential);
			networkRepository.save(network);
		}

		network.authCredential.facebookAppID = authCredential.facebookAppID;
		network.authCredential.facebookAppSecret = authCredential.facebookAppSecret;
		network.authCredential.googleWebAppID = authCredential.googleWebAppID;
		network.authCredential.googleWebAppSecret = authCredential.googleWebAppSecret;
		network.authCredential.googleAndroidAppID = authCredential.googleAndroidAppID;
		network.authCredential.googleAndroidAppSecret = authCredential.googleAndroidAppSecret;
		network.authCredential.googleAppleAppID = authCredential.googleAppleAppID;
		network.authCredential.googleAppleAppSecret = authCredential.googleAppleAppSecret;

		networkRepository.save(network);
	}

	public AuthCredential getAuthCredentials() {
		return  authCredentialRepository.findAuthCredentialByTenantId(TenantContextHolder.getCurrentTenantId());
	}
}