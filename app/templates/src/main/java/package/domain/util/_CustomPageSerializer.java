package <%=packageName%>.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.atteo.evo.inflector.English;

import java.io.IOException;

/**
 *
 */
public class CustomPageSerializer extends JsonSerializer<CustomPage> {
    @Override
    public void serialize(CustomPage value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();

        jgen.writeObjectField(English.plural(value.getType().getSimpleName().toLowerCase()), value.getPage().getContent());
        jgen.writeObjectFieldStart("meta");
        jgen.writeStringField("sort", value.getPage().getSort().toString());
        jgen.writeNumberField("size", value.getPage().getSize());
        jgen.writeNumberField("number", value.getPage().getNumber());
        jgen.writeNumberField("totalPages", value.getPage().getTotalPages());
        jgen.writeNumberField("numberOfElements", value.getPage().getNumberOfElements());
        jgen.writeNumberField("totalElements", value.getPage().getTotalElements());
        jgen.writeBooleanField("firstPage", value.getPage().isFirstPage());
        jgen.writeBooleanField("lastPage", value.getPage().isLastPage());
        jgen.writeEndObject();

        jgen.writeEndObject();
    }
}
