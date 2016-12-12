package ch.heigvd.gamification.web.filter;

import ch.heigvd.gamification.util.JWTUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String header = req.getHeader("Authorization");
        if (!JWTUtils.checkJWTHeader(header)) {
            System.out.println("No valid JWT");
            resp.setStatus(401);
            return;
        }

        String token = JWTUtils.getJWTValue(header);
        System.out.println(token);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
