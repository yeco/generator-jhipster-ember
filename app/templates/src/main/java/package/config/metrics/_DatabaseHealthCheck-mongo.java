package <%=packageName%>.config.metrics;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.CommandResult;
import <%=packageName%>.config.MetricsConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Metrics HealthCheck for the Database.
 */
@Slf4j
@Configuration("database")
@AutoConfigureAfter(MetricsConfiguration.class)
public class DatabaseHealthCheck extends HealthCheck {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Result check() {
        try {
            CommandResult commandResult = mongoTemplate.executeCommand("{ dbStats: 1, scale: 1024 }");

            return Result.healthy("Server: " + commandResult.get("serverUsed").toString() + " Db: " + commandResult.get("db"));
        } catch (Exception e) {
            log.debug("Cannot connect to Database: {}", e);
            return Result.unhealthy("Cannot connect to Database : " + e.getMessage());
        }
    }
}
