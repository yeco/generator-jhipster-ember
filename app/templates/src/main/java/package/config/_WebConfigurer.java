package <%=packageName%>.config;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import <%=packageName%>.security.OAuth2ExceptionMixin;
import <%=packageName%>.web.filter.CachingHttpHeadersFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import org.tuckey.web.filters.urlrewrite.gzip.GzipFilter;

import javax.inject.Inject;
import javax.servlet.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Slf4j
@Configuration
public class WebConfigurer implements ServletContextInitializer {

    @Inject
    private Environment env;

    @Inject
    private MetricRegistry metricRegistry;

    @Inject
    private HealthCheckRegistry healthCheckRegistry;

    @Value("<%= _.unescape('\$\{server.port:9990}')%>")
    private int port;

    @Bean
    public HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(mappingJackson2HttpMessageConverter());
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2JsonView = new MappingJackson2HttpMessageConverter();
        mappingJackson2JsonView.setObjectMapper(objectMapper());
        return mappingJackson2JsonView;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.addMixInAnnotations(OAuth2Exception.class, OAuth2ExceptionMixin.class);

        return objectMapper;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory(this.port);
        factory.addConnectorCustomizers(connector -> {
            connector.setProperty("bindOnInit", "true");
        });
        return factory;
    }
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        log.info("Web application configuration, using profiles: {}", Arrays.toString(env.getActiveProfiles()));
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);

        initMetrics(servletContext, disps);
        if (env.acceptsProfiles(Constants.SPRING_PROFILE_PRODUCTION)) {
            initUrlRewriteProductionFilter(servletContext, disps);
        }
        initCachingHttpHeadersFilter(servletContext, disps);
        initGzipFilter(servletContext, disps);

        log.info("Web application fully configured");
    }

    /**
     * Initializes the GZip filter.
     */
    private void initGzipFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering GZip Filter");

        FilterRegistration.Dynamic compressingFilter = servletContext.addFilter("gzipFilter", new GzipFilter());
        Map<String, String> parameters = new HashMap<>();

        compressingFilter.setInitParameters(parameters);

        compressingFilter.addMappingForUrlPatterns(disps, true, "*.css");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.json");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.html");
        compressingFilter.addMappingForUrlPatterns(disps, true, "*.js");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/api/v1/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/metrics/*");
        compressingFilter.addMappingForUrlPatterns(disps, true, "/info");

        compressingFilter.setAsyncSupported(true);
    }

    private void initUrlRewriteProductionFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering tuckey urlrewritefilter");

        FilterRegistration.Dynamic urlRewriteFilter = servletContext.addFilter("urlRewriteFilter", new UrlRewriteFilter());
        urlRewriteFilter.setInitParameter("confPath", "urlrewrite.xml");

        urlRewriteFilter.addMappingForUrlPatterns(disps, true, "/*");
    }

    /**
     * Initializes the cachig HTTP Headers Filter.
     */
    private void initCachingHttpHeadersFilter(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Registering Cachig HTTP Headers Filter");
        FilterRegistration.Dynamic cachingHttpHeadersFilter =
                servletContext.addFilter("cachingHttpHeadersFilter",
                        new CachingHttpHeadersFilter());

        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.woff");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.png");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.gif");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.jpg");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.css");
        cachingHttpHeadersFilter.addMappingForUrlPatterns(disps, true, "*.js");
        cachingHttpHeadersFilter.setAsyncSupported(true);
    }

    /**
     * Initializes Metrics.
     */
    private void initMetrics(ServletContext servletContext, EnumSet<DispatcherType> disps) {
        log.debug("Initializing Metrics registries");
        servletContext.setAttribute(InstrumentedFilter.REGISTRY_ATTRIBUTE,
                metricRegistry);
        servletContext.setAttribute(MetricsServlet.METRICS_REGISTRY,
                metricRegistry);
        servletContext.setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY,
                healthCheckRegistry);

        log.debug("Registering Metrics Filter");
        FilterRegistration.Dynamic metricsFilter = servletContext.addFilter("webappMetricsFilter",
                new InstrumentedFilter());

        metricsFilter.addMappingForUrlPatterns(disps, true, "/*");
        metricsFilter.setAsyncSupported(true);

        log.debug("Registering Metrics Servlet");
        ServletRegistration.Dynamic metricsAdminServlet =
                servletContext.addServlet("metricsServlet", new MetricsServlet());

        metricsAdminServlet.addMapping("/metrics/*");
        metricsAdminServlet.setAsyncSupported(true);
        metricsAdminServlet.setLoadOnStartup(2);

        log.debug("Registering HealthCheck Servlet");
        ServletRegistration.Dynamic healthCheckServlet =
                servletContext.addServlet("healthCheckServlet", new HealthCheckServlet());

        healthCheckServlet.addMapping("/health/*");
        healthCheckServlet.setAsyncSupported(true);
        healthCheckServlet.setLoadOnStartup(2);
    }
}
