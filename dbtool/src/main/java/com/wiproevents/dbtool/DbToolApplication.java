package com.wiproevents.dbtool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import reactor.Environment;

import java.util.ArrayList;
import java.util.List;


@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAutoConfiguration(exclude = {SocialWebAutoConfiguration.class, DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@ComponentScan(basePackages = {"com.wiproevents.dbtool", "com.wiproevents.aop"})
@EntityScan(basePackages = "com.wiproevents.entities")
public class DbToolApplication implements CommandLineRunner {

    /**
     * The environment.
     *
     * @return the environment.
     */
    @Bean
    public Environment env() {
        return Environment.initializeIfEmpty()
                .assignErrorJournal();
    }

    @Autowired
    private ConfigurableApplicationContext context;

    @Autowired
    private CollectionTool collectionTool;

    /**
     * Register consumers and start fire list resources event.
     *
     * @param args the command arguments.
     * @throws Exception throws if any error happens
     */
    @Override
    public void run(String... args) throws Exception {
        if (args.length >= 2) {
            if (args[0].equals("create") && args[1].equals("collections")) {
                collectionTool.createAllCollections();
            } else if (args[0].equals("drop") && args[1].equals("collections")) {
                collectionTool.dropAllCollections();
            } else if (args[0].equals("dump")) {
                List<String> array = new ArrayList<>();
                for (int i = 1; i < args.length; i++) {
                    array.add(args[i]);
                }
                collectionTool.dumpCollections(array);
            } else if (args[0].equals("load")) {
                collectionTool.loadAllCollections();
            }
        }

        System.exit(SpringApplication.exit(context));
    }

    /**
     * The main entry point of the application.
     *
     * @param args the arguments
     * @throws InterruptedException throws if error to interrupt
     */
    public static void main(String[] args) throws InterruptedException {
        SpringApplication app = new SpringApplication(DbToolApplication.class);
        // no need to use web
        app.setWebEnvironment(false);
        app.run(args);
    }
}
