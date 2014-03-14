package <%=packageName%>.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableJpaRepositories("<%=packageName%>.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
    @Autowired
    private Environment environment;

    @Value("<%= _.unescape('\$\{database.url}')%>")
    private String databaseUrl;
    @Value("<%= _.unescape('\$\{database.dataSourceClassName}')%>")
    private String dataSourceClassName;

    @Bean
    public DataSource dataSource() throws Exception {
        return new HikariDataSource(parseDatabaseUrl(databaseUrl));
    }

    @Bean(name = {"org.springframework.boot.autoconfigure.AutoConfigurationUtils.basePackages"})
    public List<String> getBasePackages() {
        List<String> basePackages = new ArrayList<>();
        basePackages.add("<%=packageName%>.domain");
        return basePackages;
    }

    @Bean
    public SpringLiquibase liquibase() throws Exception {
        log.debug("Configuring Liquibase");
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource());
        liquibase.setChangeLog("classpath:config/liquibase/db-changelog.xml");
        liquibase.setContexts("development, production");
        return liquibase;
    }

    private HikariConfig parseDatabaseUrl(String url) throws Exception {
        URI dbUri = new URI(url);
        String username = "";
        String password = "";
        if (dbUri.getUserInfo() != null) {
            username = dbUri.getUserInfo().split(":")[0];
            password = dbUri.getUserInfo().split(":")[1];
        }
        String dbUrl;

        if (url.contains("postgres")) {
            dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
        } else if (url.contains("h2")) {
            dbUrl = "jdbc:h2:mem:" + dbUri.getPath();
        } else {
            throw new RuntimeException("No URL known for: [ " + url + " ]");
        }

        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceClassName);
        if (environment.acceptsProfiles("heroku")) {
            config.addDataSourceProperty("ssl", true);
            config.addDataSourceProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
        }
        config.addDataSourceProperty("url", dbUrl);
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.setConnectionTestQuery("SELECT 1");

        return config;
    }
}
