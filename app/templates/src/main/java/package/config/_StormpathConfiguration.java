package <%=packageName%>.config;

import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.application.ApplicationList;
import com.stormpath.sdk.application.Applications;
import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.client.ClientBuilder;
import com.stormpath.spring.security.cache.SpringCacheManager;
import com.stormpath.spring.security.provider.DefaultGroupGrantedAuthorityResolver;
import com.stormpath.spring.security.provider.StormpathAuthenticationProvider;
import org.apache.commons.lang.StringUtils;
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
    @Value("<%= _.unescape('\$\{stormpath.app.name}')%>")
    private String stormpathAppName;
    @Value("<%= _.unescape('\$\{stormpath.api.key.id}')%>")
    private String stormpathApiKeyId;
    @Value("<%= _.unescape('\$\{stormpath.api.key.secret}')%>")
    private String stormpathApiKeySecret;
    @Value("<%= _.unescape('\$\{stormpath.api.key.file.location}')%>")
    private String stormpathApiKeyFileLocation;

    @Autowired
    private CacheManager cacheManager;

    @Bean
    public Application stormpathApplication() {
        Client c = stormpathClient();
        Application app = null;

        final ApplicationList applications = c.getCurrentTenant().getApplications(Applications.where(Applications.name().eqIgnoreCase(stormpathAppName)));

        for (Application application : applications) {
            //Prevent case sensitive mistakes
            if (application.getName().equals(stormpathAppName)) {
                app = application;
            }
        }

        if (app == null) {
            app = c.instantiate(Application.class);
            app.setName(stormpathAppName);
            app = c.getCurrentTenant().createApplication(Applications.newCreateRequestFor(app).createDirectory().build());
        }

        return app;
    }

    @Bean
    public Client stormpathClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        if (StringUtils.isNotEmpty(stormpathApiKeyId) && StringUtils.isNotEmpty(stormpathApiKeySecret)) {
            clientBuilder.setApiKey(stormpathApiKeyId, stormpathApiKeySecret);
        } else {
            clientBuilder.setApiKeyFileLocation(stormpathApiKeyFileLocation);
        }
        return clientBuilder.setCacheManager(new SpringCacheManager(cacheManager)).build();
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
        sap.setApplicationRestUrl(stormpathApplication().getHref());
        sap.setGroupGrantedAuthorityResolver(authorityResolver());
        return sap;
    }
}
