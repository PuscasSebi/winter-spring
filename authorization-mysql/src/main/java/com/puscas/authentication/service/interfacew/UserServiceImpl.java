package com.puscas.authentication.service.interfacew;



import com.puscas.authentication.controller.model.UserDto;
import com.puscas.authentication.model.Role;
import com.puscas.authentication.model.User;
import com.puscas.authentication.repository.RoleDetailRepository;
import com.puscas.authentication.repository.UserDetailRepository;
import com.puscas.authentication.service.UserAlreadyExistException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserDetailRepository userRepo;
    private final RoleDetailRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        log.info("saving new user to the db={}", user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("saving role to db={}", role);
        return roleRepo.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", username, roleName);
        Optional<User> user = userRepo.findByUsername(username);
        Optional<Role> role = roleRepo.findByName(roleName);

        role.ifPresent((r) ->
                user.map(u -> u.getRoles().add(r)));
    }

    @Override
    public User getUser(String username) {
        log.info("Fetching username={}", username);
        return userRepo.findByUsername(username).orElse(null);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching all users");
        List<User> all = userRepo.findAll();
        log.info("found {} users", all.size());
        return all;
    }

    @Override
    public List<Role> getRoles() {
        log.info("Fetching all users");
        List<Role> all = roleRepo.findAll();
        log.info("found {} users", all.size());
        return all;
    }

    @Override
    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        }
        User user = new User();
        user.setUsername(userDto.getEmail());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);

        return this.saveUser(user);// to get pasword encoded stuff..
    }

    private boolean emailExist(String email) {
        return userRepo.findByUsername(email).isPresent();
    }
}
