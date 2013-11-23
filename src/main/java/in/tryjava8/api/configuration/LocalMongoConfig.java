package in.tryjava8.api.configuration;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@Configuration
@Profile("local")
public class LocalMongoConfig implements MongoConfig {

    @Bean
    @Override
    public MongoDbFactory mongoDbFactory() {
        Mongo mongo = null;
        try {
            mongo = new Mongo();
        } catch (Exception e) {
            throw new BeanCreationException("Unable to create bean ", e);
        }

        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, "tryjava8");
        return mongoDbFactory;
    }

}
