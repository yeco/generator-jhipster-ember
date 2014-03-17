package <%=packageName%>.security;

import com.stormpath.spring.security.provider.StormpathUserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        StormpathUserDetails userDetails = (StormpathUserDetails) authentication.getPrincipal();
        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        Map<String, Object> info = new HashMap<>();
        info.put("username", userDetails.getUsername());

        result.setAdditionalInformation(info);
        return result;
    }
}
