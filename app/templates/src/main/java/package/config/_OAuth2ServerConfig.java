package <%=packageName%>.config;

import <%=packageName%>.security.UserApprovalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.JwtTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 */
@Configuration
public class OAuth2ServerConfig  {
    private static final String RESOURCE_ID = "<%=baseName%>";

    @Configuration
    @EnableResourceServer
    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
        @Override
        public void configure(OAuth2ResourceServerConfigurer resources) {
            resources.resourceId(RESOURCE_ID);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http.authorizeRequests()
                    .expressionHandler(new OAuth2WebSecurityExpressionHandler())
                    .antMatchers("/health", "/info", "/index.html", "/scripts/**", "/styles/**", "/images/**").permitAll()
                    .antMatchers("/env").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/trace").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/dump").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/beans").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/metrics").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/shutdown").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/metrics/**").access("#oauth2.denyOAuthClient() and hasRole('USER,ADMIN') or #oauth2.hasScope('read')")
                    .antMatchers("/api/v1/**").access("#oauth2.denyOAuthClient() and hasRole('USER') or #oauth2.hasScope('read,write')")
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                    .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                    .csrf().requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/**")).disable();
             //@formatter:on
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
        private TokenStore tokenStore = new InMemoryTokenStore();

        @Autowired
        private OAuth2RequestFactory requestFactory;

        @Autowired
        @Qualifier("authenticationManagerBean")
        private AuthenticationManager authenticationManager;

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Value("<%= _.unescape('\$\{jwt.token.signing-key}')%>")
        private String jwtTokenSigningKey;
        @Value("<%= _.unescape('\$\{jwt.token.verification-key}')%>")
        private String jwtTokenVerificationKey;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory().withClient("web")
                    .resourceIds(RESOURCE_ID)
                    .authorizedGrantTypes("password", "authorization_code", "implicit");
        }

        public UserApprovalHandler userApprovalHandler() throws Exception {
            UserApprovalHandler handler = new UserApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(requestFactory);
            handler.setClientDetailsService(clientDetailsService);
            handler.setUseApprovalStore(true);
            return handler;
        }

        public ApprovalStore approvalStore() throws Exception {
            TokenApprovalStore store = new TokenApprovalStore();
            store.setTokenStore(tokenStore);
            return store;
        }

        private AuthorizationServerTokenServices tokenServices() {
            final JwtTokenServices jwtTokenServices = new JwtTokenServices();
            jwtTokenServices.setSigningKey(jwtTokenSigningKey);
            jwtTokenServices.setVerifierKey(jwtTokenSigningKey);
            return jwtTokenServices;
        }

        @Override
        public void configure(OAuth2AuthorizationServerConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenStore(tokenStore)
                    .tokenService(tokenServices())
                    .userApprovalHandler(userApprovalHandler())
                    .authenticationManager(authenticationManager).realm("<%=baseName%>/client");
        }
    }
}
