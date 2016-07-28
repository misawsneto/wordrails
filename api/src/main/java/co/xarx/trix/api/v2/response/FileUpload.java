package co.xarx.trix.api.v2.response;

import java.io.Serializable;

public class FileUpload implements Serializable{
	private static final long serialVersionUID = 1474032587285767669L;
	public String hash;
	public Integer id;
	public String link;
	public String filelink;
}