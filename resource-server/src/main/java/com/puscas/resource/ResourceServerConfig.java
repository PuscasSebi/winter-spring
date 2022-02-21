package com.puscas.resource;


import authentication.JwtOpaqueIssuerAuthenticationManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class ResourceServerConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtOpaqueIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver =
                new JwtOpaqueIssuerAuthenticationManagerResolver("http://localhost:9000",
                        "https://accounts.google.com", "https://dev-85071309.okta.com/oauth2/default",
                        "https://www.facebook.com"
                );

        http.mvcMatcher("/articles/**")
                .authorizeRequests()
                .mvcMatchers("/articles/**")
              //  .access("hasAuthority('SCOPE_read')")
                .authenticated()
                .and()
                .oauth2ResourceServer(oauth2 ->{ oauth2.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver);
                            //   oauth2.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(opaqueTokenIntrospector));
                        }
                )
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;
        return http.build();
    }
}