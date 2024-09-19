package com.hongik.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import java.util.Optional;

@Configuration
public class SecurityLoginSwagger {

    @Bean
    OpenApiCustomizer springSecurityLoginEndpointCustomizer(ApplicationContext applicationContext) {
        FilterChainProxy filterChainProxy = applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
        return openAPI -> {
            for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
                Optional<UsernamePasswordAuthenticationFilter> optionalFilter =
                        filterChain.getFilters().stream()
                                .filter(UsernamePasswordAuthenticationFilter.class::isInstance)
                                .map(UsernamePasswordAuthenticationFilter.class::cast)
                                .findAny();
                if (optionalFilter.isPresent()) {
                    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = optionalFilter.get();
                    Operation operation = new Operation();
                    Schema<?> schema = new ObjectSchema()
                            .addProperties(usernamePasswordAuthenticationFilter.getUsernameParameter(), new StringSchema()._default("email@email.com"))
                            .addProperties(usernamePasswordAuthenticationFilter.getPasswordParameter(), new StringSchema()._default("password"));
                    RequestBody requestBody = new RequestBody().content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType().schema(schema)));
                    operation.requestBody(requestBody);
                    ApiResponses apiResponses = new ApiResponses();
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()),
                            new ApiResponse().description(HttpStatus.OK.getReasonPhrase())
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().example("{\"token\":\"sample-jwt-token\"}"))));

                    apiResponses.addApiResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                            new ApiResponse().description(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().example("{\"error\":\"UNAUTHORIZED\"}"))));

                    operation.responses(apiResponses);
                    operation.addTagsItem("User Controller - 유저 컨트롤러");
                    operation.summary("로그인");

                    PathItem pathItem = new PathItem().post(operation);
                    openAPI.getPaths().addPathItem("/api/v1/user/sign-in", pathItem);
                }
            }
        };
    }
}
