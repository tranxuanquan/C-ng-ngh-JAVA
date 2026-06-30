package vn.edu.eaut.lab6.listener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;
@WebListener
public class SessionLogListener implements HttpSessionListener {
    private static final String ACTIVE_SESSIONS = "activeSessions";
    private final AtomicInteger activeSessions = new AtomicInteger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        int count = activeSessions.incrementAndGet();
        se.getSession().getServletContext().setAttribute(ACTIVE_SESSIONS, count);
        System.out.println("Session moi duoc tao: " + se.getSession().getId()
                + " | So session dang hoat dong: " + count);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        int count = activeSessions.updateAndGet(current -> current > 0 ? current - 1 : 0);
        se.getSession().getServletContext().setAttribute(ACTIVE_SESSIONS, count);
        System.out.println("Session da bi huy: " + se.getSession().getId()
                + " | So session dang hoat dong: " + count);
    }
}
