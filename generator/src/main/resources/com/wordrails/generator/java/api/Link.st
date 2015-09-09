Link(package) ::=

<<package $package$;

import java.io.Serializable;

public class Link implements Serializable {
	private static final long serialVersionUID = 6623665429584551928L;
	
	public String rel;
	public String href;
}>>