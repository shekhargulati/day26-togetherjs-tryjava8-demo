package in.tryjava8.api.configuration;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.Mongo;

@Configuration
@Profile("openshift")
public class OpenShiftMongoConfig implements MongoConfig {

    @Override
    public MongoDbFactory mongoDbFactory() {
        String openshiftMongoDbHost = System.getenv("OPENSHIFT_MONGODB_DB_HOST");
        int openshiftMongoDbPort = Integer.parseInt(System.getenv("OPENSHIFT_MONGODB_DB_PORT"));
        String username = System.getenv("OPENSHIFT_MONGODB_DB_USERNAME");
        String password = System.getenv("OPENSHIFT_MONGODB_DB_PASSWORD");
        Mongo mongo = null;
        try {
            mongo = new Mongo(openshiftMongoDbHost, openshiftMongoDbPort);
        } catch (Exception e) {
            throw new BeanCreationException("Unable to create a bean", e);
        }

        UserCredentials userCredentials = new UserCredentials(username, password);
        String databaseName = System.getenv("OPENSHIFT_APP_NAME");
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(mongo, databaseName, userCredentials);
        return mongoDbFactory;
    }

}
