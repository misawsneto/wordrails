package co.xarx.trix.services.analytics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RequestWrapper {
    Map<String, String> headers;
    Map<String, String> parameters;
    String localName;
    String remoteAddr;

    public RequestWrapper(){
        headers = new HashMap<>();
        parameters = new HashMap<>();
    }

    public RequestWrapper(HttpServletRequest request){
        headers = new HashMap<>();
        parameters = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();

        while(headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }

        Enumeration params = request.getParameterNames();
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            parameters.put(paramName, request.getParameter(paramName));
        }

        this.localName = request.getLocalName();
        this.remoteAddr = request.getRemoteAddr();
    }

    public String getHeader(String headerName){
        return headers.get(headerName);
    }
}
