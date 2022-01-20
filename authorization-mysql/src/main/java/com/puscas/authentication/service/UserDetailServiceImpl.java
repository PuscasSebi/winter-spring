package com.puscas.authentication.service;

import com.puscas.authentication.converter.Converter;
import com.puscas.authentication.model.AuthUserDetail;
import com.puscas.authentication.model.User;
import com.puscas.authentication.repository.UserDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

	@Autowired
	private UserDetailRepository userDetailRepository;

	private AccountStatusUserDetailsChecker accountStatusUserDetailsChecker = new AccountStatusUserDetailsChecker();


	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

		Optional<User> optionalUser = userDetailRepository.findByUsername(name);

		optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username or password wrong"));

		UserDetails userDetails = Converter.convertToUser(optionalUser.get());
		accountStatusUserDetailsChecker.check(userDetails);
		return userDetails;
	}
	
//	public static void main(String[] args) {
//			String generatedHash = BCrypt.hashpw("ahasan", BCrypt.gensalt(12));
//			System.out.println(generatedHash);
//	}
}
