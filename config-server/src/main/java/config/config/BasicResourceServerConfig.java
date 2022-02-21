package config.config;


import authentication.JwtOpaqueIssuerAuthenticationManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


public class BasicResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtOpaqueIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver =
                new JwtOpaqueIssuerAuthenticationManagerResolver("http://localhost:9000",
                        "https://accounts.google.com", "https://dev-85071309.okta.com/oauth2/default",
                        "https://www.facebook.com"
                );

        http.httpBasic()
        ;
        return http.build();
    }
}