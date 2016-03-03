package co.xarx.trix.domain;


import co.xarx.trix.annotation.SdkExclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@lombok.Getter
@lombok.Setter
@SdkExclude
@Entity
@Table(name="androidapp", uniqueConstraints = @UniqueConstraint(columnNames = "tenantId"))
public class DBAndroidApp extends BaseEntity implements AndroidApp {

	private static final long serialVersionUID = 6858234763575719281L;

	public static final Integer MDPI_SIZE = 48;
	public static final Integer HDPI_SIZE = 72;
	public static final Integer XHDPI_SIZE = 96;
	public static final Integer XXHDPI_SIZE = 144;
	public static final Integer XXXHDPI_SIZE = 192;
	public static final Integer ORIGINAL_SIZE = 512;

	private String projectName;
	private String appName;
	private String keyAlias;
	private String packageSuffix;
	private String host;

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Lob
	private String shortDescription;

	@Lob
	private String fullDescription;
	private String videoUrl;

	private String apkUrl;

	@NotNull
	@ManyToOne(cascade = CascadeType.REMOVE)
	private File icon;
}
