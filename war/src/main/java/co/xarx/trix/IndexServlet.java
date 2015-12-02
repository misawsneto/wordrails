package co.xarx.trix;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {
		"/*"
})
public class IndexServlet extends DispatcherServlet {
	public IndexServlet(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}
}
