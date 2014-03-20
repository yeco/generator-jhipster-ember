package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
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
@TypeDefs({
        @TypeDef(name = "pg-uuid", typeClass = org.hibernate.type.PostgresUUIDType.class, defaultForType = java.util.UUID.class),
})
@MappedSuperclass
public abstract class Base implements Persistable<UUID>, Serializable {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @NotNull
    private UUID id;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return null == id;
    }
}
