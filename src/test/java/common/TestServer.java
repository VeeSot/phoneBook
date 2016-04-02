package common;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * veesot on 4/2/16.
 */
public class TestServer {
    public Server testServer;

    public TestServer(ServletContextHandler context) {
        this.testServer = new Server(8080);
        this.context = context;
    }

    private ServletContextHandler context;

    public void start() throws Exception {
        ResourceHandler resource_handler = new ResourceHandler();
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        testServer.setHandler(handlers);
        testServer.start();
    }

    public void stop() throws Exception {
        testServer.stop();
    }
}
