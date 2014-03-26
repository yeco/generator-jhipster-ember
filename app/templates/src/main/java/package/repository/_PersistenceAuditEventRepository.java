package <%=packageName%>.repository;

import <%=packageName%>.domain.AuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for the AuditEvent entity.
 */
public interface PersistenceAuditEventRepository extends JpaRepository<AuditEvent, UUID> {

    List<AuditEvent> findByPrincipal(String principal);

    List<AuditEvent> findByPrincipalAndAuditEventDateGreaterThan(String principal, LocalDateTime after);

    @Query("select p from AuditEvent p where p.auditEventDate >= ?1 and p.auditEventDate <= ?2")
    List<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate);
}
