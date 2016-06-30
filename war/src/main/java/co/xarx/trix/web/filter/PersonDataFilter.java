package co.xarx.trix.web.filter;

import co.xarx.trix.api.PersonData;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.domain.Network;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.InitService;
import co.xarx.trix.services.NetworkService;
import co.xarx.trix.services.PerspectiveService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("personDataFilter")
public class PersonDataFilter implements Filter{

	@Autowired
	private InitService initService;
	@Autowired
	private NetworkService networkService;
	@Autowired
	private PerspectiveService perspectiveService;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	@Qualifier("simpleMapper")
	public ObjectMapper simpleMapper;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	@Transactional(readOnly=true)
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = networkService.getNetworkFromHost(request.getHeader("Host"));

		if(network != null){
			Integer stationId = initService.getStationIdFromCookie(request);
			PersonData personData = initService.getInitialData(baseUrl, network);

			PersonData data = initService.getData(personData, stationId);
			if (data.defaultStation != null) {
				TermPerspectiveView termPerspectiveView = perspectiveService.termPerspectiveView(null, null,
						data.defaultStation.defaultPerspectiveId, 0, 10);
				request.setAttribute("termPerspectiveView", simpleMapper.writeValueAsString(termPerspectiveView));
			}

			request.setAttribute("personData", simpleMapper.writeValueAsString(data) + "");
			request.setAttribute("networkName", data.network.name);
			request.setAttribute("networkId", data.network.id);
			if (data.network.faviconHash != null)
				request.setAttribute("faviconLink", amazonCloudService.getPublicImageURL(data.network.faviconHash));
			request.setAttribute("networkDesciption", "");
			request.setAttribute("networkKeywords", "");
			request.setAttribute("personData", simpleMapper.writeValueAsString(data));
			request.setAttribute("networkName", data.network.name);
		}

		chain.doFilter(request, res);
	}

	@Override
	public void destroy() {
	}
}
