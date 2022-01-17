package com.puscas.authentication.repository;

import com.puscas.authentication.model.UserCredentials;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface UserCredentialsRepository extends CassandraRepository<UserCredentials, String> {
}
