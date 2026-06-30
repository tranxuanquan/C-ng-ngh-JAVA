package vn.edu.eaut.lab7.listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebListener
public class AppLogListener implements ServletContextListener, HttpSessionListener {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log("Application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log("Application stopped");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log("Session created: " + se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        log("Session destroyed: " + se.getSession().getId());
    }

    private void log(String message) {
        System.out.println("[APP-LOG " + LocalDateTime.now().format(FORMATTER) + "] " + message);
    }
}
