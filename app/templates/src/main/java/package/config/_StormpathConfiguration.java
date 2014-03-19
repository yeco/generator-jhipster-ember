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
    private static final String APP_NAME = "<%=baseName%>";

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

        final ApplicationList applications = c.getCurrentTenant().getApplications(Applications.where(Applications.name().eqIgnoreCase(APP_NAME)));

        for (Application application : applications) {
            //Prevent case sensitive mistakes
            if (application.getName().equals(APP_NAME)) {
                app = application;
            }
        }

        if (app == null) {
            app = c.instantiate(Application.class);
            app.setName(APP_NAME);
            app = c.getCurrentTenant().createApplication(Applications.newCreateRequestFor(app).createDirectory().build());
        }

        return app;
    }

    @Bean
    public Client stormpathClient() {
        final ClientBuilder clientBuilder = new ClientBuilder();
        if (StringUtils.isNotEmpty(stormpathApiKeyFileLocation)) {
            clientBuilder.setApiKeyFileLocation(stormpathApiKeyFileLocation);
        } else {
            clientBuilder.setApiKey(stormpathApiKeyId, stormpathApiKeySecret);
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
