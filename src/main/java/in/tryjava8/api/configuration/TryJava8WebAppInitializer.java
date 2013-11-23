package in.tryjava8.api.configuration;

import in.tryjava8.api.listeners.ServiceContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class TryJava8WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootCtx = new AnnotationConfigWebApplicationContext();
        rootCtx.getEnvironment().setActiveProfiles("openshift");
        rootCtx.register(ApplicationConfig.class);

        servletContext.addListener(new ContextLoaderListener(rootCtx));

        AnnotationConfigWebApplicationContext webCtx = new AnnotationConfigWebApplicationContext();
        webCtx.getEnvironment().setActiveProfiles("openshift");
        webCtx.register(WebAppConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webCtx);
        servletContext.addListener(new ServiceContextListener());
        Dynamic reg = servletContext.addServlet("tryjava8-api", dispatcherServlet);
        reg.setLoadOnStartup(1);
        reg.addMapping("/api/*");
    }

}
