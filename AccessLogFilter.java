package vn.edu.eaut.lab6.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebFilter("/*")
public class AccessLogFilter implements Filter {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        String user = "anonymous";
        if (session != null && session.getAttribute("username") != null) {
            user = session.getAttribute("username").toString();
        }

        System.out.println("Access log | URI: " + req.getRequestURI()
                + " | Method: " + req.getMethod()
                + " | User: " + user
                + " | Time: " + LocalDateTime.now().format(FORMATTER));

        chain.doFilter(request, response);
    }
}
