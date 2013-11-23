package in.tryjava8.api.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class ServiceContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent context) {
        ServletContext sc = context.getServletContext();
        WebApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(sc);
        if (springContext.containsBean("mongoTemplate")) {
            MongoTemplate mt = (MongoTemplate) springContext.getBean("mongoTemplate");
            if (mt != null) {
                DB db = mt.getDb();
                if (db != null) {
                    Mongo mongo = db.getMongo();
                    if (mongo != null) {
                        mongo.close();
                    }
                }
            }
        }

        System.gc();

        do {
            try { // https://jira.mongodb.org/browse/JAVA-400
                Thread.sleep(5000);
                break;
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        } while (true);

        java.beans.Introspector.flushCaches();
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

    }

}