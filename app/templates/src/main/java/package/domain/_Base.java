package <%=packageName%>.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
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
public class Base implements Persistable<UUID>, Serializable {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @NotNull
    private UUID id;

    @Override
    public boolean isNew() {
        return null == id;
    }
}
