package co.xarx.trix.domain;

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
public class Notification extends BaseEntity {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;

    @Column(name = "entity_id")
    public Integer entityId;

    @NotEmpty
    @Size(min=1,max=500)
    public String message;

    public Boolean sendNotification;

    @NotEmpty
    @Enumerated(EnumType.STRING)
    public NotificationType type;

    @ManyToMany
    public Person target;
}
