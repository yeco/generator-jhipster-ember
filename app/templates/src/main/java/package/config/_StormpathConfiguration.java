package <%=packageName%>.config;

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.spring.security.cache.SpringCacheManager;
import com.stormpath.spring.security.provider.DefaultGroupGrantedAuthorityResolver;
import com.stormpath.spring.security.provider.StormpathAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
public class StormpathConfiguration {
    @Value("<%= _.unescape('\$\{stormpath.api.key.id}')%>")
    private String stormpathApiKeyId;
    @Value("<%= _.unescape('\$\{stormpath.api.key.secret}')%>")
    private String stormpathApiKeySecret;
    @Value("<%= _.unescape('\$\{stormpath.application.url}')%>")
    private String stormpathApplicationUrl;

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public Client stormpathClient() {
        return new ClientBuilder()
                .setApiKey(stormpathApiKeyId, stormpathApiKeySecret)
                .setCacheManager(new SpringCacheManager(cacheManager)).build();
    }

    @Bean
    public DefaultGroupGrantedAuthorityResolver authorityResolver() {
        DefaultGroupGrantedAuthorityResolver authorityResolver = new DefaultGroupGrantedAuthorityResolver();
        authorityResolver.getModes().add(DefaultGroupGrantedAuthorityResolver.Mode.NAME);
        return authorityResolver;
    }

    @Bean
    public StormpathAuthenticationProvider authenticationProvider() {
        StormpathAuthenticationProvider sap = new StormpathAuthenticationProvider();
        sap.setClient(stormpathClient());
        sap.setApplicationRestUrl(stormpathApplicationUrl);
        sap.setGroupGrantedAuthorityResolver(authorityResolver());
        return sap;
    }
}
