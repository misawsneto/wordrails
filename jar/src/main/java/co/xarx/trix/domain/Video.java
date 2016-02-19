package co.xarx.trix.domain;

import co.xarx.trix.annotation.SdkExclude;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@Entity
public class Video extends BaseEntity {

	@Id
	@Setter(AccessLevel.NONE)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public Integer duration;

	@SdkExclude
	public File file;

}
