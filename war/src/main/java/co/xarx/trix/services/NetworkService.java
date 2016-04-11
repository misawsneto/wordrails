package co.xarx.trix.services;

import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class NetworkService {

	private Map<String, Integer> tenantIds; //key=tenantId, value=networkId
	private Map<String, Integer> domains; //key=domain, value=networkId

	private NetworkRepository networkRepository;

	@Autowired
	public NetworkService(NetworkRepository networkRepository) {
		this.networkRepository = networkRepository;
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
}
