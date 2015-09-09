Batch(package, facade, entities) ::=

<<package $package$;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.CLASS, property="command")
abstract class Command {
	abstract Response execute(List<Response> results, $facade$ facade);
	
	String toUriList(String uri, List<Response> results) {
		if (uri == null) {
			return null;
		} else {
			StringBuilder uriList = new StringBuilder();
			appendUri(uriList, uri, results);
			return uriList.toString();
		}
	}

	String toUriList(List<String> uris, List<Response> results) {
		if (uris == null) {
			return null;
		} else {
			StringBuilder uriList = new StringBuilder();
			for (String uri : uris) {
				appendUri(uriList, uri, results);
				uriList.append("\n");
			}
			return uriList.toString();
		}
	}
	
	String replace(String uri, List<Response> results) {
		if (uri == null) {
			return null;
		} else {
			StringBuilder replacedUri = new StringBuilder();
			appendUri(replacedUri, uri, results);
			return replacedUri.toString();
		}
	}

	<T extends Collection<String\>\> T replace(T uris, List<Response> results) {
		if (uris != null) {
			List<String> replacedUris = new ArrayList<String>(uris.size());
			for (String uri : uris) {
				String replacedUri = replace(uri, results);
				replacedUris.add(replacedUri);
			}
			uris.clear();
			uris.addAll(replacedUris);
		}
		return uris;
	}
	
	private void appendUri(StringBuilder uriList, String uri, List<Response> results) {
		for (int i = 0; i < uri.length(); ++i) {
			char c = uri.charAt(i);
			if (c == '{') {
				int j = uri.indexOf("}", i);
				String indexString = uri.substring(i + 1, j);
				int index = Integer.parseInt(indexString);
				Response result = results.get(index);
				String location = result.getLocation().toString();
				uriList.append(location); 
				i = j + 1;
			} else {
				uriList.append(c);
			}
		}
	}
}

$entities:{entity | $BatchEntityCommands(facade, entity)$}; separator="\n\n"$>>