package dbService;

import org.hibernate.cfg.Configuration;

/**
 * @author veesot on 1/21/16.
 */
public class Config {
    private static final String hibernate_show_sql = "false";
    private static final String generate_statistics = "false";

    public static Configuration getPgConfiguration(Class clazz) {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(clazz);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/phone_book");
        configuration.setProperty("hibernate.connection.username", "test");
        configuration.setProperty("hibernate.connection.password", "test");
        configuration.setProperty("hibernate.show_sql", hibernate_show_sql);
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.setProperty("hibernate.hbm2ddl.generate_statistics", generate_statistics);
        return configuration;
    }

}


