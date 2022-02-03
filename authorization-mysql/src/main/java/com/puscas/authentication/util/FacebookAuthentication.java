package com.puscas.authentication.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class FacebookAuthentication implements Authentication {
    private final Map<String, Object> info;
    Principal principal;

    public FacebookAuthentication(Map<String,Object> tokenInfo) {
        principal = new UsernamePasswordAuthenticationToken(tokenInfo.get("email"),"");
        this.info = tokenInfo;
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
        return String.valueOf(info.get("name"));
    }
}
