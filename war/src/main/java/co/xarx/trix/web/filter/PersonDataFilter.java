package co.xarx.trix.web.filter;

import co.xarx.trix.api.PersonData;
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

			request.setAttribute("networkId", data.network.id);
			if (data.network.faviconHash != null)
				request.setAttribute("faviconLink", amazonCloudService.getPublicImageURL(data.network.faviconHash));
			request.setAttribute("networkDesciption", "");
			request.setAttribute("networkKeywords", "");
			request.setAttribute("personDataObject", data);
			request.setAttribute("personData", simpleMapper.writeValueAsString(data));
			request.setAttribute("networkName", data.network.name);
			request.setAttribute("trackingId",
			data.network.trackingId != null ?
					"(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');" +
					"ga('create', '" + data.network.trackingId + "', { 'cookieDomain': 'none' })":"");
		}

		chain.doFilter(request, res);
	}

	@Override
	public void destroy() {
	}
}
