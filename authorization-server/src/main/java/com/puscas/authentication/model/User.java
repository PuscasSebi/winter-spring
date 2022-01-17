package com.puscas.authentication.model;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("users")
public class User {
    @PrimaryKey
    private UUID id;
    private String username;
    private String password;
    private String email;
    private List<String> grantedAuthority;
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public List<String> getGrantedAuthority() {
        return grantedAuthority;
    }

    public void setGrantedAuthority(List<String> grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }
}

