package <%=packageName%>.domain.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Data
@JsonSerialize(using = JsonListSerializer.class)
public class JsonList<D> {
    private List<D> entities = new ArrayList<>();
    private Class<D> type;
    private Map<String, Object> meta = new HashMap<>();

    public JsonList(Class<D> type, D entity) {
        this.type = type;
        this.entities.add(entity);
    }

    public JsonList(Class<D> type, D entity, Map<String, Object> meta) {
        this.type = type;
        this.entities.add(entity);
        this.meta = meta;
    }

    public JsonList(Class<D> type, List<D> entities) {
        this.type = type;
        this.entities = entities;
    }

    public JsonList(Class<D> type, List<D> entities, Map<String, Object> meta) {
        this.entities = entities;
        this.type = type;
        this.meta = meta;
    }
}
