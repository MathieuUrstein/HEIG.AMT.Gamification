package ch.heigvd.gamification.util;

public interface URIs {
    String AUTH = "/auth/";
    String BADGES = "/badges/";
    String USERS = "/users/";
    String EVENTS = "/events/";
    String POINT_SCALES = "/pointScales/";
    String REGISTER = "/register/";
    String RULES = "/rules/";
    String EVENT_RULES = RULES + "/events/";
    String TRIGGER_RULES = RULES + "/triggers/";
    String DOCUMENTATION = "/";

    String SWAGGER_HTML = "/swagger-ui.html";
    String SWAGGER_UI_RESOURCES = "/webjars/springfox-swagger-ui/";
    String SWAGGER_RESOURCES = "/swagger-resources";
    String V2_API_DOCS = "/v2/api-docs";
    String STATIC = "/static/";
}
