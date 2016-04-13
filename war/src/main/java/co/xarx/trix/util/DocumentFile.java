package co.xarx.trix.util;

public class DocumentFile {

	public java.io.File file;
	public String hash;

	public DocumentFile(java.io.File file, String hash) {
		this.file = file;
		this.hash = hash;
	}
}
