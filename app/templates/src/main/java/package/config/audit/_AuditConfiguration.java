package <%=packageName%>.config.audit;

import <%=packageName%>.domain.AuditEvent;
import <%=packageName%>.repository.PersistenceAuditEventRepository;
import <%=packageName%>.service.AuditEventConverter;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Configuration
public class AuditConfiguration {
    @Bean
    public AuditEventRepository auditEventRepository(final PersistenceAuditEventRepository persistenceAuditEventRepository) {
        return new AuditEventRepository() {

            @Inject
            private AuditEventConverter auditEventConverter;

            @Override
            public List<org.springframework.boot.actuate.audit.AuditEvent> find(String principal, Date after) {
                final List<AuditEvent> auditEvents;
                if (principal == null && after == null) {
                    auditEvents = persistenceAuditEventRepository.findAll();
                } else if (after == null) {
                    auditEvents = persistenceAuditEventRepository.findByPrincipal(principal);
                } else {
                    Instant instant = Instant.ofEpochMilli(after.getTime());
                    auditEvents = persistenceAuditEventRepository.findByPrincipalAndAuditEventDateGreaterThan(principal, LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                }

                return auditEventConverter.convertToAuditEvent(auditEvents);
            }

            @Override
            public void add(org.springframework.boot.actuate.audit.AuditEvent event) {
                AuditEvent auditEvent = new AuditEvent();
                auditEvent.setPrincipal(event.getPrincipal());
                auditEvent.setAuditEventType(event.getType());
                Instant instant = Instant.ofEpochMilli(event.getTimestamp().getTime());
                auditEvent.setAuditEventDate(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
                auditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()));

                persistenceAuditEventRepository.save(auditEvent);
            }
        };
    }
}
