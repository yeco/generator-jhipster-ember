package <%=packageName%>.domain;

import java.io.Serializable;

/**
 *
 */
public interface Resource<ID extends Serializable> {
    ID getId();

    void setId(ID id);
}
