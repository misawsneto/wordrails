package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NetworkService {

	private Map<String, Integer> tenantIds; //key=tenantId, value=networkId
	private Map<String, Integer> domains; //key=domain, value=networkId

	private NetworkRepository networkRepository;
	private EmailService emailService;

	@Autowired
	public NetworkService(NetworkRepository networkRepository, EmailService emailService) {
		this.networkRepository = networkRepository;
		this.emailService = emailService;
		tenantIds = Maps.newConcurrentMap();
		domains = Maps.newConcurrentMap();

		List<Network> networks = networkRepository.findAll();
		for (Network n : networks) {
			tenantIds.put(n.tenantId, n.id);
			if (n.domain != null) domains.put(n.domain, n.id);
		}
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

	public String getNetworkValidationTemplate() throws IOException {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		String template = getEmailTemplate();
		return template.replaceAll("\\{\\{invitationTemplate}}", network.invitationMessage);
	}

	public String getNetworkInvitationTemplate() throws IOException {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		String template = getEmailTemplate();
		return template.replaceAll("\\{\\{invitationTemplate}}", network.invitationMessage);
	}

	public String getEmailTemplate() throws IOException {
		String templateFile;
		templateFile = "complete-subscription-email.html";
		return emailService.loadTemplateHTML(templateFile);
	}
}