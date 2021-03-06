package ch.heigvd.gamification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerDocumentationConfig {

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("AMT - gamification")
                .description(
                        "The API specification for the AMT - gamification project. All endpoints are implicitely " +
                        "prefixed by __/api__ for the sake of readability.\n\n" +
                        "All the endpoints except `/auth/` and `/register/` are protected by a JWT-based " +
                        "authentication, thus the following `401` errors can occur:\n\n" +
                        "- Error code 7: No token.\n" +
                        "- Error code 8: JWT invalid."
                )
                .license("")
                .licenseUrl("http://unlicense.org")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("ch.heigvd.gamification.web.api"))
                .build()
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .useDefaultResponseMessages(false)
                .ignoredParameterTypes(ApiIgnore.class)
                .apiInfo(apiInfo());
    }

}
