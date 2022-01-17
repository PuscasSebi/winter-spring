package com.puscas.authentication.service;

import com.puscas.authentication.model.UserCredentials;
import com.puscas.authentication.repository.UserCredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Autowired
    private UserCredentialsRepository userCredentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserCredentials> byId = userCredentialsRepository.findById(username);

        if(!byId.isPresent()){
            throw new UsernameNotFoundException("no user found for username= "+ username);
        }
        return byId.get();
    }
}
