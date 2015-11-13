package co.xarx.trix.picasso;

import java.io.IOException;
import java.util.StringTokenizer;

import co.xarx.trix.api.WordRails;
import retrofit.client.Header;
import retrofit.mime.TypedInput;
import android.net.Uri;

import com.squareup.picasso.Downloader;

public class FileContentsDownloader implements Downloader {
	private WordRails wordRails;

	public FileContentsDownloader(WordRails wordRails) {
		this.wordRails = wordRails;
	}

	@Override
	public Response load(Uri uri, boolean localCacheOnly) throws IOException {
		String path = uri.getPath();
		Integer id = new Integer(path);
		retrofit.client.Response response = wordRails.getFileContents(id);
		TypedInput body = response.getBody();
		boolean loadedFromCache = getLoadedFromCache(response);
		return new Response(body.in(), loadedFromCache, body.length());
	}
	
	private boolean getLoadedFromCache(retrofit.client.Response response) {
		for (Header header : response.getHeaders()) {
			String name = header.getName();
			if ("OkHttp-Response-Source".equals(name)) {
				String value = header.getValue();
				StringTokenizer tokenizer = new StringTokenizer(value, " ");
				if (tokenizer.hasMoreTokens()) {
					String token1 = tokenizer.nextToken();
					if ("CACHE".equals(token1)) {
						return true;
					}
					if (tokenizer.hasMoreTokens()) {
						String token2 = tokenizer.nextToken();
						return "CONDITIONAL_CACHE".equals(token1) && "304".equals(token2);
					} else {
						return false;
					}
				}
				break;
			}
		}
		return false;
	}
}