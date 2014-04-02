package <%=packageName%>.repository;

import <%=packageName%>.domain.AuditEvent;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the AuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends MongoRepository<AuditEvent, ObjectId> {

    List<AuditEvent> findByPrincipal(String principal);

    List<AuditEvent> findByPrincipalAndAuditEventDateGreaterThan(String principal, LocalDateTime after);
}
