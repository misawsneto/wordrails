package co.xarx.trix.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"network_id", "token"}))
public class PersonNetworkToken {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer id;

//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.NO)
	@NotEmpty
	public String token;
	
	@ManyToOne
	@JoinColumn(name = "person_id")
	public Person person;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "network_id")
	public Network network;
	
	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	public Date createdAt;
	
//	@Latitude
	public Double lat;
	
//	@Longitude
	public Double lng;

	@PrePersist
	void onCreate() {
		createdAt = new Date();
		updatedAt = new Date();
	}

	@JsonFormat(shape=JsonFormat.Shape.NUMBER)
	@Temporal(TemporalType.TIMESTAMP)
	public Date updatedAt;

	@PreUpdate
	void onUpdate() {
		updatedAt = new Date();
	}

	@Override
	public String toString() {
		if(token != null)
			return token.toString();
		else
			return super.toString();
	}
}

