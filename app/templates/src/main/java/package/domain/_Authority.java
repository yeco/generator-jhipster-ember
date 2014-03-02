package <%=packageName%>.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;<% if (hibernateCache != 'no') { %>
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;<% } %>

/**
 * An authority (a security role) used by Spring Security.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "authorities")<% if (hibernateCache != 'no') { %>
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %>
public class Authority extends Base {
    @NotNull
    @Size(min = 0, max = 50)
    private String name;
}
