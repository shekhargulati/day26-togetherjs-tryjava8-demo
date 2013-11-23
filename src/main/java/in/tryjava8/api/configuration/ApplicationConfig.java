package in.tryjava8.api.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ComponentScan("in.tryjava8.api")
public class ApplicationConfig {

    @Autowired
    private MongoConfig mongoConfig;

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoConfig.mongoDbFactory());
        return mongoTemplate;
    }

}
