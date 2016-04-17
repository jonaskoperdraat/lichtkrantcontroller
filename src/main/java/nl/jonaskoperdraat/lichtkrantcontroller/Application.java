package nl.jonaskoperdraat.lichtkrantcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

/**
 * Main application. Instantiates the Spring Boot Application
 */
@SpringBootApplication
public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        LOG.trace("Trace message!");
        LOG.debug("Debug message!");
        LOG.info("Info message!");
        LOG.warn("Warn message!");
        LOG.error("Error message!");

        LOG.info("Application.main()");
        ApplicationContext ctx = SpringApplication.run(Application.class, args);

        LOG.debug("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for(String beanName : beanNames) {
            LOG.debug(beanName);
        }
    }
}
