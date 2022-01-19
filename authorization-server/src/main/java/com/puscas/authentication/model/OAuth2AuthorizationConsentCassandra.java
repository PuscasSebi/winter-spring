package com.puscas.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;

import java.util.List;
import java.util.stream.Collectors;

@Table("oauth2_consent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AuthorizationConsentCassandra {

    @PrimaryKeyColumn(ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    @Column("registered_client")
    private String registeredClient;
    @Column("principal_name")
    @PrimaryKeyColumn(ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String principalName;
    @Column("authorities")
    private List<String> authorities;

    @Transient
    public  OAuth2AuthorizationConsent toOauth2Consent(){
        OAuth2AuthorizationConsent.Builder builder = OAuth2AuthorizationConsent.withId(registeredClient, principalName);
        authorities.forEach((auth) -> builder.authority(new SimpleGrantedAuthority(auth)));
        return builder.build();
    }

    public static OAuth2AuthorizationConsentCassandra fromOauth2Consents(OAuth2AuthorizationConsent consent){
        List<String> collect = consent.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.toList());
        return new OAuth2AuthorizationConsentCassandra(consent.getRegisteredClientId(), consent.getPrincipalName(), collect);
    }
}
