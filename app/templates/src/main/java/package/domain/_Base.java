package <%=packageName%>.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
<% if (prodDatabaseType == 'postgresql') { %>import org.hibernate.annotations.Type;<% } %>
import org.springframework.data.domain.Persistable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 *
 */
@Data
@MappedSuperclass
public abstract class Base implements Persistable<UUID>, Serializable {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @NotNull
    <% if (prodDatabaseType == 'postgresql') { %>@Type(type="pg-uuid")<% } %>
    private UUID id;

    @Override
    public boolean isNew() {
        return null == id;
    }
}
