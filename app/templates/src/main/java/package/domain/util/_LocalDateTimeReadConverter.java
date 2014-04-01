package <%=packageName%>.domain.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 *
 */
public class LocalDateTimeReadConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return LocalDateTime.parse(source);
    }
}
