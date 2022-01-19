package com.puscas.authentication.repository;


import com.puscas.authentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface RoleDetailRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
