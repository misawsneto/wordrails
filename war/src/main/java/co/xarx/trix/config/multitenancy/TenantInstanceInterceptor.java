//package co.xarx.trix.config.multitenancy;
//
//import co.xarx.trix.services.NetworkService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.ContainerResponseContext;
//import javax.ws.rs.container.ContainerResponseFilter;
//import java.io.IOException;
//
//@Component
////@Provider
//public class TenantInstanceInterceptor implements ContainerRequestFilter, ContainerResponseFilter {
//
//	private NetworkService networkService;
//
//	@Autowired
//	public TenantInstanceInterceptor(NetworkService networkService) {
//		this.networkService = networkService;
//	}
//
//	@Override
//	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
//		TenantContextHolder.setCurrentTenantId(null);
//	}
//
//	@Override
//	public void filter(ContainerRequestContext request) throws IOException {
//		String tenantId = networkService.getTenantFromHost(request.getHeaderString("Host"));
//		TenantContextHolder.setCurrentTenantId(tenantId);
//	}
//}
