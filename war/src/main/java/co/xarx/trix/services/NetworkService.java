package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Network;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.StringUtil;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

		List<Object[]> networks = networkRepository.findIdsAndDomain();
		for (Object[] n : networks) {
			Integer id = (Integer) n[0];
			String domain = (String) n[1];
			String tenantId = (String) n[2];
			tenantIds.put(tenantId, id);
			if (domain != null)
				domains.put(domain, id);
		}
	}

	public void addTenant(String tenant, Integer id){
		tenantIds.put(tenant, id);
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
		return template.replaceAll("\\{\\{invitationMessage}}", network.invitationMessage);
	}

	public String getEmailTemplate() throws IOException {
		String templateFile;
		templateFile = "invitation-template.html";
		return FileUtil.loadFileFromResource(templateFile);
	}

	public String getValidationMessage() throws IOException {
	    String tenantId = TenantContextHolder.getCurrentTenantId();
        Network network = networkRepository.findByTenantId(tenantId);
        String htmlTemplate = FileUtil.loadFileFromResource("validation-template.html");

        if(network.getValidationMessage() == null || network.getValidationMessage().isEmpty()){
            String message = FileUtil.loadFileFromResource("default_validation_text.txt");
            return htmlTemplate.replaceAll("\\{\\{validationMessage}}", message);
        }

        return htmlTemplate.replaceAll("\\{\\{validationMessage}}", network.getValidationMessage());
    }

	public static Constants.MobilePlatform getDeviceFromRequest(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");

		if (userAgent != null && userAgent.contains("WordRailsIOSClient"))
			return Constants.MobilePlatform.APPLE;
		else if ("OkHttp".equals(userAgent))
			return Constants.MobilePlatform.ANDROID;
		else
			return null;
	}

	public Network getNetwork(){
		Network network = networkRepository.findByTenantId(TenantContextHolder.getCurrentTenantId());
		network.setTenantId(TenantContextHolder.getCurrentTenantId());
		return network;
	}
}