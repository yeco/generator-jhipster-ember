package <%=packageName%>.web.rest;

import <%=packageName%>.domain.PersistentAuditEvent;
import <%=packageName%>.repository.PersistenceAuditEventRepository;
import <%=packageName%>.web.propertyeditors.LocaleDateTimeEditor;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST controller for getting the audit events.
 */
@RestController
@RequestMapping("/api/v1/auditEvents")
public class AuditEventsResource extends AbstractRestResource<PersistentAuditEvent, UUID, PersistentAuditEvent.AuditEventWrapper> {

    @Autowired
    private PersistenceAuditEventRepository persistenceAuditEventRepository;

    @Override
    protected Class<PersistentAuditEvent> entityClass() {
        return PersistentAuditEvent.class;
    }

    @Override
    protected PersistentAuditEvent.AuditEventWrapper entityWrapper(PersistentAuditEvent entity) {
        return new PersistentAuditEvent.AuditEventWrapper(entity);
    }

    @Override
    protected PagingAndSortingRepository<PersistentAuditEvent, UUID> repository() {
        return persistenceAuditEventRepository;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocaleDateTimeEditor("yyyy-MM-dd", false));
    }

    @Override
    public ResponseEntity<PersistentAuditEvent.AuditEventWrapper> create(PersistentAuditEvent.AuditEventWrapper v) throws Exception {
        throw new UnsupportedOperationException("Request method 'POST' not supported");
    }

    @Override
    public ResponseEntity<PersistentAuditEvent.AuditEventWrapper> update(@PathVariable("id") UUID uuid, PersistentAuditEvent.AuditEventWrapper v) throws Exception {
        throw new UnsupportedOperationException("Request method 'PUT' not supported");
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") UUID uuid) throws Exception {
        throw new UnsupportedOperationException("Request method 'DELETE' not supported");
    }
}
