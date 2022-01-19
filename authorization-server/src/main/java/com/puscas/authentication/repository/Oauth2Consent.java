package com.puscas.authentication.repository;


import com.puscas.authentication.model.OAuth2AuthorizationConsentCassandra;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.Optional;

public interface Oauth2Consent extends CassandraRepository<OAuth2AuthorizationConsentCassandra, String> {


    @Query("SELECT * from oauth2_consent where registered_client=?0 and principal_name=?1")
    Optional<OAuth2AuthorizationConsentCassandra> findById(String registeredClient, String principalName);
}
