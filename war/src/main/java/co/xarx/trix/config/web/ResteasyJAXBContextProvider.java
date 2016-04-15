package co.xarx.trix.config.web;

import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
@Component
public class ResteasyJAXBContextProvider implements ContextResolver<JAXBContext> {


	@Override
	public JAXBContext getContext(Class<?> type) {
		try {
			return JAXBContext.newInstance("org.springframework.data.domain.jaxb");
		} catch (JAXBException e) {
			return null;
		}
	}
}