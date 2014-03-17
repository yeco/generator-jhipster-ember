package <%=packageName%>.domain.util;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 *
 */
@Data
@JsonSerialize(using = CustomPageSerializer.class)
public class CustomPage<T> {
    private Page<T> page;
    private Class<T> type;

    public CustomPage(Class<T> type) {
        this.type = type;
    }
}
