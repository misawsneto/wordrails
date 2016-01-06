package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Network;
import co.xarx.trix.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Network.class)
@Component
public class NetworkEventHandler {
	
	@Autowired
	private CacheService cacheService;
	
	@HandleAfterSave
	public void handleAfterSave(Network network){
		cacheService.updateNetwork(network.id);
		cacheService.updateNetwork(network.getTenantId());
	}
}