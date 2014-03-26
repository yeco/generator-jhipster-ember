package <%=packageName%>.web.rest;

import <%=packageName%>.domain.AuditEvent;
import <%=packageName%>.repository.PersistenceAuditEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping("/api/v1/auditEvents")
public class AuditEventsResource extends AbstractRestResource<AuditEvent, UUID, AuditEvent.AuditEventWrapper> {

    @Autowired
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Override
    protected Class<AuditEvent> entityClass() {
        return AuditEvent.class;
    }

    @Override
    protected AuditEvent.AuditEventWrapper entityWrapper(AuditEvent entity) {
        return new AuditEvent.AuditEventWrapper(entity);
    }

    @Override
    protected PagingAndSortingRepository<AuditEvent, UUID> repository() {
        return persistenceAuditEventRepository;
    }

    @Override
    public ResponseEntity<AuditEvent.AuditEventWrapper> create(AuditEvent.AuditEventWrapper v) throws Exception {
        throw new UnsupportedOperationException("Request method 'POST' not supported");
    }

    @Override
    public ResponseEntity<AuditEvent.AuditEventWrapper> update(@PathVariable("id") UUID uuid, AuditEvent.AuditEventWrapper v) throws Exception {
        throw new UnsupportedOperationException("Request method 'PUT' not supported");
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") UUID uuid) throws Exception {
        throw new UnsupportedOperationException("Request method 'DELETE' not supported");
    }
}
