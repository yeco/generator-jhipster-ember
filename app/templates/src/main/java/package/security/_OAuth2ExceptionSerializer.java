package <%=packageName%>.security;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import <%=packageName%>.web.rest.RestError;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import java.io.IOException;

/**
 *
 */
public class OAuth2ExceptionSerializer extends StdSerializer<OAuth2Exception> {

    public static final String STORMPATH_ERROR_MESSAGE_PREFIX = "HTTP 400, Stormpath 400 (mailto:support@stormpath.com): ";

    public OAuth2ExceptionSerializer() {
        super(OAuth2Exception.class);
    }

    @Override
    public void serialize(OAuth2Exception value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        RestError restError = RestError.UNAUTHORIZED_ACCESS_ERROR;
        restError.setStatus(value.getHttpErrorCode());
        restError.setMessage(cleanupMessage(value.getMessage()));
        jgen.writeObject(restError);
    }

    private String cleanupMessage(String message) {
        if(StringUtils.isNotEmpty(message) && message.startsWith(STORMPATH_ERROR_MESSAGE_PREFIX)) {
            return message.replace(STORMPATH_ERROR_MESSAGE_PREFIX, "");
        }
        return message;
    }
}
