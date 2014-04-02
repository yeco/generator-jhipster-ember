package <%=packageName%>.domain.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

/**
 *
 */
public class LocalDateTimeWriteConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime source) {
        return source.toString();
    }
}
