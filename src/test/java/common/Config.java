package common;

import org.hibernate.cfg.Configuration;

/**
 * @author veesot on 1/21/16.
 */
public class Config {
    public static Configuration getTestDbConfiguration() {
        return new Configuration().configure("hibernateTest.cfg.xml");
    }
}


