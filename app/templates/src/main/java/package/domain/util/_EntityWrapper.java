package <%=packageName%>.domain.util;

import <%=packageName%>.domain.Resource;

import java.io.Serializable;

/**
 *
 */
public interface EntityWrapper<E extends Resource> extends Serializable {
    E getEntity();
}
