package <%=packageName%>.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.atteo.evo.inflector.English;

import java.io.IOException;

/**
 *
 */
public class JsonListSerializer extends JsonSerializer<JsonList> {
    @Override
    public void serialize(JsonList value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeArrayFieldStart(English.plural(value.getType().getSimpleName().toLowerCase()));
        for (Object o : value.getEntities()) {
            jgen.writeObject(o);
        }
        jgen.writeEndArray();

        if (!value.getMeta().isEmpty()) {
            jgen.writeObjectField("meta", value.getMeta());
        }

        jgen.writeEndObject();
    }
}
