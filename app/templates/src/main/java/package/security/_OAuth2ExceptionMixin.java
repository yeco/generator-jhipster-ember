package <%=packageName%>.security;

import org.springframework.security.oauth2.common.exceptions.OAuth2ExceptionJackson2Deserializer;

/**
 *
 */
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = OAuth2ExceptionSerializer.class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = OAuth2ExceptionJackson2Deserializer.class)
public class OAuth2ExceptionMixin {
}
