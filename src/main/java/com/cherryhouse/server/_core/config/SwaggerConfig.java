package com.cherryhouse.server._core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@OpenAPIDefinition(
        info = @Info(
                title = "체리집 API 명세서",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI openAPI(){

                //JWT 동작을 위한 설정
                SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .name("Authorization");
                SecurityRequirement requirement = new SecurityRequirement().addList("bearerAuth");

                return new OpenAPI().components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                        .security(Collections.singletonList(requirement));
        }
}
