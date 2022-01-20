package com.puscas.authentication.converter;
import com.puscas.authentication.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;


public class Converter {

    public static User convertToUser (com.puscas.authentication.model.User user){
        String username = user.getUsername();
        String password = user.getPassword();
        User springUser = new User(username, password,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                getGrantedAuthoritiesFromUser(user));

        return springUser;
    }

    public static List<GrantedAuthority> getGrantedAuthoritiesFromUser(com.puscas.authentication.model.User authUserDetail) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        authUserDetail.getRoles().forEach(role -> {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPermissions().forEach(permission -> {
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
            });

        });
        return grantedAuthorities;
    }
}
