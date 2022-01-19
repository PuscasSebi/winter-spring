package com.puscas.authentication.service.interfacew;




import com.puscas.authentication.model.Role;
import com.puscas.authentication.model.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    //do pagination here , ofc
    List<User> getUsers();


    List<Role> getRoles();
}
