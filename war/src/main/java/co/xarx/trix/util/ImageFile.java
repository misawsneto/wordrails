package co.xarx.trix.util;

public class ImageFile {

	public java.io.File file;
	public Integer height;
	public Integer width;
	public String hash;
	public boolean vertical;

	public ImageFile(java.io.File file, Integer height, Integer width, String hash) {
		this.file = file;
		this.height = height;
		this.width = width;
		this.hash = hash;
		this.vertical = height > width;
	}
}
