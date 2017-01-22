package ch.heigvd.gamification.web.filter;

import ch.heigvd.gamification.dao.ApplicationRepository;
import ch.heigvd.gamification.error.ErrorDescription;
import ch.heigvd.gamification.error.ErrorsCodes;
import ch.heigvd.gamification.model.Application;
import ch.heigvd.gamification.util.JWTUtils;
import ch.heigvd.gamification.util.URIs;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class AuthenticationFilter implements Filter {
    private final ApplicationRepository applicationRepository;
    private static Logger LOG = Logger.getLogger(AuthenticationFilter.class.getName());

    @Autowired
    public AuthenticationFilter(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI().substring(request.getContextPath().length());
        String token = JWTUtils.extractToken(request.getHeader("Authorization"));

        boolean doc = uri.equals(URIs.DOCUMENTATION) ||
                uri.equals(URIs.SWAGGER_HTML) ||
                uri.startsWith(URIs.SWAGGER_UI_RESOURCES) ||
                uri.startsWith(URIs.SWAGGER_RESOURCES) ||
                uri.equals(URIs.V2_API_DOCS);

        boolean authRequest = uri.equals(URIs.AUTH);

        if (doc || uri.equals(URIs.REGISTER) || (authRequest && token == null)) {
            chain.doFilter(servletRequest, response);
            return;
        }

        if (token == null) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorsCodes.NO_TOKEN,
                    ErrorsCodes.NO_TOKEN_MESSAGE);
            return;
        }

        try {
            DecodedJWT jwt = JWTUtils.verifyToken(token);

            // if token is not valid
            if (jwt == null) {
                if (authRequest) {
                    chain.doFilter(request, response);
                    return;
                }
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorsCodes.JWT_INVALID,
                        ErrorsCodes.JWT_INVALID_MESSAGE);
                return;
            }

            if (authRequest) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, ErrorsCodes.ALREADY_AUTHENTICATED,
                        ErrorsCodes.ALREADY_AUTHENTICATED_MESSAGE);
                return;
            }

            Application app = applicationRepository.findByName(jwt.getSubject());
            servletRequest.setAttribute("application", app);

            chain.doFilter(request, response);
        } catch (JWTDecodeException exception) {
            LOG.log(Level.WARNING, "Invalid JWT format");
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorsCodes.INVALID_JWT_FORMAT,
                    ErrorsCodes.INVALID_JWT_FORMAT_MESSAGE);
        }
    }

    private void sendError(HttpServletResponse response, int status, String errorCode, String message) throws
            IOException {
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(new ErrorDescription(errorCode, message)));
    }

    @Override
    public void destroy() {
    }
}
