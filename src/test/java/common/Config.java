package common;

import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * @author veesot on 1/21/16.
 */
public class Config {
    private static final String hibernate_show_sql = "false";
    private static final String generate_statistics = "false";

    public static Configuration getH2Configuration(List<Class> clazzes) {
        Configuration configuration = new Configuration();
        clazzes.forEach(configuration::addAnnotatedClass);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:h2:./h2db");
        configuration.setProperty("connection.pool_size", "3");
        configuration.setProperty("hibernate.connection.username", "test");
        configuration.setProperty("hibernate.connection.password", "test");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.generate_statistics", generate_statistics);
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        return configuration;
    }

}


