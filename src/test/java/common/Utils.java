package common;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.UUID;

/**
 * veesot on 4/2/16.
 */
public class Utils {
    public static String getRandomNumber() {
        Random rand = new Random();
        int number = rand.nextInt(100000000);

        DecimalFormat df10 = new DecimalFormat("0000000000"); // 10 zeros for Russian number

        return "8" + df10.format(number);
    }

    public static String getRandomName() {
        return UUID.randomUUID().toString();// Yeah, it's name =)
    }
}
