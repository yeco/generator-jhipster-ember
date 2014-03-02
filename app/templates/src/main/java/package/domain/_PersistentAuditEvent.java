package <%=packageName%>.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Data
@Entity
@Table(name = "T_PERSISTENT_AUDIT_EVENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersistentAuditEvent  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private long id;

    @NotNull
    @Column(name = "principal")
    private String principal;

    @Column(name = "event_date")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime auditEventDate;

    @Column(name = "event_type")
    private String auditEventType;


    @ElementCollection
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="T_PERSISTENT_AUDIT_EVENT_DATA", joinColumns=@JoinColumn(name="event_id"))
    private Map<String, String> data = new HashMap<>();
}
