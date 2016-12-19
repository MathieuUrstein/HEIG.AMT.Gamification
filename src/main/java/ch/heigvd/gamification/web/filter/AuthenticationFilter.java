package ch.heigvd.gamification.web.filter;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.URIs;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    private final ApplicationRepository applicationRepository;

    @Autowired
    public AuthenticationFilter(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

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

        checkJWTAndDoFilter(servletRequest, servletResponse, chain);
    }

    private void checkJWTAndDoFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                                     FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = JWTUtils.extractToken(request.getHeader("Authorization"));
        if (token == null) {
            System.out.println("No JWT found");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            JWT.decode(token);

            DecodedJWT jwt = JWTUtils.verifyToken(token);
            Application app;

            if (jwt == null || (app = applicationRepository.findByName(jwt.getSubject())) == null) {
                System.out.println("JWT is not valid");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            servletRequest.setAttribute("application", app);
            chain.doFilter(servletRequest, servletResponse);

        }
        catch (JWTDecodeException exception) {
            System.out.println("Invalid JWT format");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {}
}
