package Socket;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class Listener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        // Do your thing during webapp's startup.
    	 System.out.println("listener");
         Gestore_connessioniEntrata.getInstance().startAllSocket();
    }
    public void contextDestroyed(ServletContextEvent event) {
        // Do your thing during webapp's shutdown.
    }
}