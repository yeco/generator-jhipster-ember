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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

/**
 *
 */
@TypeDefs({
        @TypeDef(name = "pg-uuid", typeClass = org.hibernate.type.PostgresUUIDType.class, defaultForType = java.util.UUID.class),
        @TypeDef(name = "localDateType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalDate.class, defaultForType = LocalDate.class),
        @TypeDef(name = "localDateTimeType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime.class, defaultForType = LocalDateTime.class),
        @TypeDef(name = "localTimeType", typeClass = org.jadira.usertype.dateandtime.threeten.PersistentLocalTime.class, defaultForType = LocalTime.class)
})
@Data
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
