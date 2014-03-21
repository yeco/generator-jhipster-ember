package <%=packageName%>.config.audit;

import <%=packageName%>.domain.AuditEvent;
import <%=packageName%>.repository.PersistenceAuditEventRepository;
import <%=packageName%>.service.AuditEventConverter;
import org.joda.time.LocalDateTime;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
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
                    auditEvents =
                            persistenceAuditEventRepository.findByPrincipalAndAuditEventDateGreaterThan(principal, new LocalDateTime(after));
                }

                return auditEventConverter.convertToAuditEvent(auditEvents);
            }

            @Override
            public void add(org.springframework.boot.actuate.audit.AuditEvent event) {
                AuditEvent auditEvent = new AuditEvent();
                auditEvent.setPrincipal(event.getPrincipal());
                auditEvent.setAuditEventType(event.getType());
                auditEvent.setAuditEventDate(new LocalDateTime(event.getTimestamp()));
                auditEvent.setData(auditEventConverter.convertDataToStrings(event.getData()));

                persistenceAuditEventRepository.save(auditEvent);
            }
        };
    }
}
