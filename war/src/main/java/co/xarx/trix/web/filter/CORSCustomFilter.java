package co.xarx.trix.web.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("corsCustomFilter")
public class CORSCustomFilter implements Filter{

	@Value("${trix.auth.header:'x-auth-trix-token'}")
	private String trixAuthHeader;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletResponse resp = (HttpServletResponse) res;
		HttpServletRequest reqt = (HttpServletRequest) req;
		String method = reqt.getMethod().toLowerCase();
		
		setCorsHeader(reqt, resp);

		if(method.equals("head") || method.equals("options")){
			resp.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		chain.doFilter(req, res);

	}
	
	private void setCorsHeader(HttpServletRequest reqt, HttpServletResponse resp){
		String origin = reqt.getHeader("Origin");
		if(origin == null || origin.isEmpty())
			origin = "*";

		resp.setHeader("Access-Control-Allow-Origin", origin);
		resp.setHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,"+trixAuthHeader);
		resp.setHeader("Access-Control-Allow-Credentials", "true");
		resp.setHeader("Access-Control-Max-Age", "3600");
		resp.setHeader("Access-Control-Allow-Headers", "Authorization,Content-Type,Content-Range,Content-Disposition,Content-Description,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers," + trixAuthHeader);
		resp.setHeader("Access-Control-Allow-Methods", "GET,POST,DELETE,PUT,HEAD,OPTIONS");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
