package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 */
@Data
public abstract class Base implements Persistable<ObjectId>, Serializable {
    @Id
    @NotNull
    private ObjectId id;

    @JsonIgnore
    @Override
    public boolean isNew() {
        return null == id;
    }
}
