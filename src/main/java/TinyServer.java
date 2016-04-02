import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * veesot on 4/2/16.
 */
public class TinyServer {
    public static void main(String[] args) throws Exception {

        Server server = new Server(8080);
        HandlerCollection handlers = new HandlerCollection();
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setResourceBase("web");
        webAppContext.setContextPath("/api/v1/");
        handlers.addHandler(webAppContext);


        // Adding the handlers to the server
        server.setHandler(handlers);

        //Run, Server, Run!
        server.start();
        server.join();
    }
}
