package authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class GoogleAuthentication implements Authentication {

    Principal principal;

    public GoogleAuthentication(Map<String,Object> tokenInfo) {
        principal = new UsernamePasswordAuthenticationToken(tokenInfo.get("email"),"");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }


    @Override
    public Object getPrincipal() {
        return principal;
    }


    @Override
    public boolean isAuthenticated() {
        return true;
    }


    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }


    @Override
    public String getName() {
        return null;
    }
}
