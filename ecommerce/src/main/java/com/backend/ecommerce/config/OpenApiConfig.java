package com.backend.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Shripad",
                        email = "shripad2002@gmail.com",
                        url = "http://localhost:4200/products"
                ),
                description = "Open API documentation for e-commerce website",
                title = "OpenAPI Specification - Shripad",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://github.com/svk1102"
                ),
                termsOfService = "Terms of Service"
        ),servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                ),@Server(
                        description = "Prod ENV",
                        url = "https://google.com"
                )
        }

)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
