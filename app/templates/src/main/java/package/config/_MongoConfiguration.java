package <%=packageName%>.config;

import com.mongodb.Mongo;
import <%=packageName%>.domain.util.LocalDateTimeReadConverter;
import <%=packageName%>.domain.util.LocalDateTimeWriteConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.Arrays;

/**
 *
 */
@Configuration
public class MongoConfiguration extends AbstractMongoConfiguration {
    @Autowired
    private MongoProperties properties;

    @Autowired
    private Mongo mongo;

    @Bean
    public GridFsTemplate gridFsTemplate() throws Exception {
        return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
    }

    @Override
    protected String getDatabaseName() {
        return properties.getMongoClientDatabase();
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongo;
    }

    @Override
    public CustomConversions customConversions() {
        return new CustomConversions(Arrays.asList(new LocalDateTimeReadConverter(), new LocalDateTimeWriteConverter()));
    }
}
