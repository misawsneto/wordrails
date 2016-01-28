package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NetworkService {

	private Map<String, Integer> tenantIds;
	private Map<String, Integer> domains;

	@Autowired
	private NetworkRepository networkRepository;

	@PostConstruct //method with no args is required
	public void init() throws ServletException {
		tenantIds = Maps.newConcurrentMap();
		domains = Maps.newConcurrentMap();

		List<Network> networks = networkRepository.findAll();
		for (Network n : networks) {
			tenantIds.put(n.subdomain, n.id);
			if (n.domain != null) domains.put(n.domain, n.id);
		}
	}

	public String getTenantFromHost(String host) {
		String tenantId = StringUtil.getSubdomainFromHost(host);
		if (domains.keySet().contains(host)) {
			TenantContextHolder.setCurrentNetworkId(domains.get(host));
			for (String s : tenantIds.keySet()) {
				if (Objects.equals(tenantIds.get(s), domains.get(host))) {
					tenantId = s;
					break;
				}
			}
		} else if (tenantIds.keySet().contains(tenantId)) {
			TenantContextHolder.setCurrentNetworkId(tenantIds.get(tenantId));
		} else {
			Network network = networkRepository.findByDomain(host);
			if (network != null) {
				TenantContextHolder.setCurrentNetworkId(network.id);
				domains.put(host, network.id);
				tenantId = network.subdomain;
			} else {
				network = networkRepository.findBySubdomain(tenantId);
				if (network != null) {
					tenantIds.put(tenantId, network.id);
				} else {
					return null;
				}
			}
		}

		return tenantId;
	}
}
