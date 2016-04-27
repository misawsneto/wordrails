package co.xarx.trix.domain;

import co.xarx.trix.util.Constants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"tenantId", "type"}))
public class PublishedApp extends BaseEntity implements Serializable {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public Constants.MobilePlatform type;

	public String publisherEmail;

	public String publisherPassword;

	public String packageName;

	public String publisherPublicName;

	public String sku;

	public String vendorId;

	@Override
	public Serializable getId() {
		return null;
	}
}
