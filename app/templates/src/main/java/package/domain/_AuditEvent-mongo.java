package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import <%=packageName%>.domain.util.EntityWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Persist AuditEvent managed by the Spring Boot actuator
 *
 * @see org.springframework.boot.actuate.audit.AuditEvent
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document
public class AuditEvent extends Base implements Resource<ObjectId> {
    @NotNull
    private String principal;

    private LocalDateTime auditEventDate;

    private String auditEventType;

    private Map<String, String> data = new HashMap<>();

    @Data
    public static class AuditEventWrapper implements EntityWrapper<AuditEvent> {
        @Valid
        private AuditEvent auditEvent;

        public AuditEventWrapper() {
        }

        public AuditEventWrapper(AuditEvent auditEvent) {
            this.auditEvent = auditEvent;
        }

        @JsonIgnore
        @Override
        public AuditEvent getEntity() {
            return auditEvent;
        }
    }
}
