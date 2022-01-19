package com.puscas.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@AllArgsConstructor
public class AuthUserDetail extends User implements UserDetails {

    public AuthUserDetail(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return getGrantedAuthoritiesFromUser(this);
    }

    public static List<GrantedAuthority> getGrantedAuthoritiesFromUser(User authUserDetail) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        authUserDetail.getRoles().forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPermissions().forEach(permission -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
            });

        });
        return grantedAuthorities;
    }


    @Override
    public String getPassword() {
        return super.getPassword();
    }


    @Override
    public String getUsername() {
        return super.getUsername();
    }


    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }


    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }


    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
