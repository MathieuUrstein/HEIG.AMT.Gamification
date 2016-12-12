package ch.heigvd.gamification.web.filter;

import ch.heigvd.gamification.util.AuthenticationUtils;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.URIs;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI().substring(request.getContextPath().length());
        if (uri.equals(URIs.AUTH) || uri.equals(URIs.REGISTER)) {
            chain.doFilter(servletRequest, response);
            return;
        }

        String token = JWTUtils.extractToken(request.getHeader("Authorization"));
        if (token == null) {
            System.out.println("No JWT found");
            response.setStatus(401);
            return;
        }

        try {
            JWT.decode(token);

            if (!JWTUtils.isTokenValid(token)) {
                System.out.println("JWT is not valid");
                response.setStatus(403);
                return;
            }

            System.out.println("JWT OK!");
            chain.doFilter(servletRequest, servletResponse);

        } catch (JWTDecodeException exception) {
            System.out.println("Invalid JWT format");
            response.setStatus(401);
        }
    }

    @Override
    public void destroy() {}
}
