package <%=packageName%>.config;

import <%=packageName%>.security.UserApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.authentication.configurers.InMemoryClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.OAuth2AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 */
@Configuration
@Order(3)
public class OAuth2ServerConfig extends WebSecurityConfigurerAdapter {
    private static final String RESOURCE_ID = "<%= appname %>";

    @Autowired
    private TokenStore tokenStore;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .expressionHandler(new OAuth2WebSecurityExpressionHandler())
                .antMatchers("/env").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/trace").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/dump").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/beans").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/metrics").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/shutdown").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/metrics/**").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                .antMatchers("/api/v1/**").access("#oauth2.denyOAuthClient() and hasRole('USER') or #oauth2.hasScope('read,write')")
                .and()
                .requestMatchers()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                        // CSRF protection is awkward for machine clients
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/**")).disable()
                .apply(new OAuth2ResourceServerConfigurer()).tokenStore(tokenStore)
                .resourceId(RESOURCE_ID);
    }

    @Configuration
    @Order(1)
    protected static class AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfigurerAdapter {
        private TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        private OAuth2RequestFactory requestFactory;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.apply(new InMemoryClientDetailsServiceConfigurer())
                    .withClient("web")
                    .authorizedGrantTypes("implicit", "password", "refresh_token", "authorization_code")
                    .scopes("read", "write")
                    .autoApprove(true)
                    .authorities("none");
        }

        @Bean
        @Override
        @Lazy
        @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        public UserApprovalHandler userApprovalHandler() throws Exception {
            UserApprovalHandler handler = new UserApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(requestFactory);
            handler.setClientDetailsService(clientDetailsService);
            handler.setUseApprovalStore(true);
            return handler;
        }

        @Bean
        public ApprovalStore approvalStore() throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        @Override
        protected void configure(OAuth2AuthorizationServerConfigurer oauthServer) throws Exception {
            oauthServer.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler())
                    .authenticationManager(authenticationManager).realm("<%= appname %>/client");
        }
    }
}