package co.edu.univalle.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class GatewayRoutes {

    @Bean
    public RouterFunction<ServerResponse> authRoutes(){
        return GatewayRouterFunctions.route("auth_service")
                .route(RequestPredicates.path("/auth/**"),HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRoutesFunction(){
        return GatewayRouterFunctions.route("user_service")
                .route(RequestPredicates.path("/usuarios/**"),HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderRoutesFunction(){
        return GatewayRouterFunctions.route("home-service")
                .route(RequestPredicates.path("/"),HandlerFunctions.http("http://localhost:8081"))
                .route(RequestPredicates.path("/inicio"),HandlerFunctions.http("http://localhost:8081"))
                .build();
    }

}