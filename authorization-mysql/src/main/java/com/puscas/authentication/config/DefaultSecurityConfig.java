package com.puscas.authentication.config;


import authentication.JwtOpaqueIssuerAuthenticationManagerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;


@EnableWebSecurity
public class DefaultSecurityConfig  {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtOpaqueIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver =
                new JwtOpaqueIssuerAuthenticationManagerResolver("http://localhost:9000",
                "https://accounts.google.com", "https://dev-85071309.okta.com/oauth2/default",
                        "https://www.facebook.com"
                        );

        http
                .authorizeRequests(authorizeRequests ->
          authorizeRequests
                  .antMatchers("/login*", "/registration*", "/error*").permitAll()
                )
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .oauth2ResourceServer(oauth2 ->{ oauth2.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver);
                    //   oauth2.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(opaqueTokenIntrospector));
                        }
                ).csrf().ignoringAntMatchers("/registration*")

        ;
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                Authentication principal = context.getPrincipal();
                Set<String> authorities = new HashSet<>();
                for (GrantedAuthority authority : principal.getAuthorities()) {
                    authorities.add(authority.getAuthority());
                }
                context.getClaims().claim("scope", authorities);
                context.getClaims().claim("email", principal.getName());
            }
        };
    }


/*    @Bean
    public AccountStatusUserDetailsChecker accountStatusUserDetailsChecker(){
        return new AccountStatusUserDetailsChecker();
    }*/

}
