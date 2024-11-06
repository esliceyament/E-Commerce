package com.esliceyament.apigateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {



    //////////////////////////////////////////blacklist in redis
    @Autowired
    private JwtUtil jwtUtil;

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization Header"));
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);

                }
                try {

                    jwtUtil.validateToken(authHeader);

                    String role = jwtUtil.extractRole(authHeader);

                    System.out.println(role);

                    String path = exchange.getRequest().getURI().getPath();

                    if (path.startsWith("/products/all") || path.startsWith("/products/search") || path.startsWith("/cart/addToCart") || path.startsWith("/cart/{productCode}")) {
                        if (!role.equals("USER") && !role.equals("STORE") && !role.equals("ADMIN")) {
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this role"));
                        }
                    } else if (path.startsWith("/products") || path.startsWith("/cart") || path.startsWith("/discount")) {
                        if (!role.equals("STORE") && !role.equals("ADMIN")) {
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this role"));
                        }
                    } else {
                        if (!role.equals("ADMIN")) {
                            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this role"));
                        }
                    }


                } catch (Exception e) {
                    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to application"));
                }
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}