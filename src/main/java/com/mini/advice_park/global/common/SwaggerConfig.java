package com.mini.advice_park.global.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "박훈수 API",
                version = "v1",
                description = "박훈수 API Docs"
        )
)
@Configuration
public class SwaggerConfig {

    private static final String JWT_AUTH_SCHEME_NAME = "AccessToken";
    private static final String REFRESH_TOKEN_SCHEME_NAME = "RefreshToken";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()

                        .addSecuritySchemes(JWT_AUTH_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(JWT_AUTH_SCHEME_NAME)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization"))

                        .addSecuritySchemes(REFRESH_TOKEN_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(REFRESH_TOKEN_SCHEME_NAME)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Refresh-Token")))

                .addSecurityItem(new SecurityRequirement().addList(JWT_AUTH_SCHEME_NAME))
                .addSecurityItem(new SecurityRequirement().addList(REFRESH_TOKEN_SCHEME_NAME));

    }

}
