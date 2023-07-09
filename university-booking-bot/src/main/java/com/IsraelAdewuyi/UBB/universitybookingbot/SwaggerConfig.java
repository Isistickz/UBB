package com.IsraelAdewuyi.UBB.universitybookingbot;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

//@Configuration
//@EnableSwagger2
//@ConfigurationProperties("app.api")
//@ConditionalOnProperty(name="app.api.swagger.enable", havingValue = "true", matchIfMissing = false)
public class SwaggerConfig {
    //    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.IsraelAdewuyi"))
                .paths(PathSelectors.any())
                .build().apiInfo(metaData());
//                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
//                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
//                .apiInfo(apiInfo());
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("University Booking Bot")
                .description("\"Uni booking bot\"")
                .version("1.1.0")
                .contact(new Contact("Israel", "i.adewuyi@innopolis.university", "same@gmail.com"))
                .build();
    }

    /**
     ...
     ...
     Getters
     Setters
     ...
     ...
     **/
}
