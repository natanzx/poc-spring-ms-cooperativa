package br.com.poc.desafio.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.poc.desafio.controller.PautaController;
import br.com.poc.desafio.controller.PautaV2Controller;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${server.address}")
    private String host;

    @Value("${server.port}")
    private Integer port;

    @Value("${server.ssl.enabled}")
    private Boolean isHttps;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Bean
    public GroupedOpenApi apiV1() {

        final Info info = new Info()
            .title("API - Desafio")
            .version("1.0.0")
            .contact(descriptionContact())
            .description("Esta API reproduz a funcionalidade de assembleia.")
            .termsOfService("http://www.termsofservice.url")
            .license(descriptionLicense());

        final String[] paths = { "/v1/**" };

        return GroupedOpenApi.builder()
            .group("v1")
            .packagesToScan(PautaController.class.getPackageName())
            .pathsToMatch(paths)
            .addOpenApiCustomizer(openApi -> openApi
                .info(info)
                .servers(List.of(getServer())))
            .build();
    }

    @Bean
    public GroupedOpenApi apiV2() {

        final Info info = new Info()
            .title("API - Desafio")
            .version("2.0.0")
            .contact(descriptionContact())
            .description("Esta API reproduz a funcionalidade de assembleia.")
            .termsOfService("http://www.termsofservice.url")
            .license(descriptionLicense());

        final String[] paths = { "/v2/**" };

        return GroupedOpenApi.builder()
            .group("v2")
            .packagesToScan(PautaV2Controller.class.getPackageName())
            .pathsToMatch(paths)
            .addOpenApiCustomizer(openApi -> openApi
                .info(info)
                .servers(List.of(getServer())))
            .build();
    }

    private Contact descriptionContact() {
        return new Contact()
            .name("Natanael Dias Silva")
            .email("natanzx@gmail.com")
            .url("https://github.com/natanzx");
    }

    private License descriptionLicense() {
        return new License()
            .name("License")
            .url("http://www.license.url");
    }

    private Server getServer() {
        Server devServer = new Server();
        devServer.setUrl(String.format("%s://%s:%d", isHttps ? "https" : "http", host, port));
        devServer.setDescription("Server URL in " + activeProfile + " environment");
        return devServer;
    }
}
