package in.tryjava8.api.configuration;

import org.springframework.data.mongodb.MongoDbFactory;

public interface MongoConfig {

    public MongoDbFactory mongoDbFactory();

}
