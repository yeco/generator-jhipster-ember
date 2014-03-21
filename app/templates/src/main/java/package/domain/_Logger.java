package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import <%=packageName%>.domain.util.EntityWrapper;
import lombok.Data;

import javax.validation.Valid;

/**
 *
 */
@Data
public class Logger implements Resource<String> {
    private String id;
    private String level;

    public Logger() {
    }

    public Logger(ch.qos.logback.classic.Logger logger) {
        this.id = logger.getName();
        this.level = logger.getLevel() == null ? null : logger.getLevel().levelStr;
    }

    @Data
    public static class LoggerWrapper implements EntityWrapper<Logger> {
        @Valid
        private Logger logger;

        public LoggerWrapper() {
        }

        public LoggerWrapper(Logger logger) {
            this.logger = logger;
        }

        @JsonIgnore
        @Override
        public Logger getEntity() {
            return logger;
        }
    }
}
