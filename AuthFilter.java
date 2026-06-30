package vn.edu.eaut.lab6.filter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
@WebFilter(urlPatterns = {"/dashboard", "/students", "/student-form.jsp", "/welcome.jsp"})
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("username") != null;
        if (!loggedIn) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (isAdminFunction(req) && !"ADMIN".equals(role)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher("/403.jsp").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAdminFunction(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        if ("/student-form.jsp".equals(servletPath)) {
            return true;
        }
        if (!"/students".equals(servletPath)) {
            return false;
        }

        String method = request.getMethod();
        String action = request.getParameter("action");
        return "POST".equalsIgnoreCase(method) || "edit".equals(action);
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter destroyed");
    }
}
