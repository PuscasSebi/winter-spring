package com.puscas.authentication.config;

import com.puscas.authentication.util.JwtOpaqueIssuerAuthenticationManagerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;

@EnableWebSecurity
public class DefaultSecurityConfig  {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        JwtOpaqueIssuerAuthenticationManagerResolver jwtIssuerAuthenticationManagerResolver =
                new JwtOpaqueIssuerAuthenticationManagerResolver("http://localhost:9000",
                "https://accounts.google.com", "https://dev-85071309.okta.com/oauth2/default"
                        );

        OpaqueTokenIntrospector opaqueTokenIntrospector = new SpringOpaqueTokenIntrospector(
                "https://www.googleapis.com/oauth2/v1/tokeninfo",
"246861652401-bhs3d3435gof3dfggfkl6qk1190qpj9q.apps.googleusercontent.com",
                "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"

        );

        http
                .authorizeRequests(authorizeRequests ->
          authorizeRequests
                  .antMatchers("/login*").permitAll()
                  .anyRequest().authenticated()
                )
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .oauth2ResourceServer(oauth2 ->{ oauth2.authenticationManagerResolver(jwtIssuerAuthenticationManagerResolver);
                    //   oauth2.opaqueToken(opaqueTokenConfigurer -> opaqueTokenConfigurer.introspector(opaqueTokenIntrospector));
                        }
                )

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


/*    @Bean
    public AccountStatusUserDetailsChecker accountStatusUserDetailsChecker(){
        return new AccountStatusUserDetailsChecker();
    }*/

}
