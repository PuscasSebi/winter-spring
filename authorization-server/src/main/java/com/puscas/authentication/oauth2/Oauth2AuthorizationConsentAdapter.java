package com.puscas.authentication.oauth2;

import com.puscas.authentication.model.OAuth2AuthorizationConsentCassandra;
import com.puscas.authentication.repository.Oauth2Consent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Oauth2AuthorizationConsentAdapter implements OAuth2AuthorizationConsentService {

    @Autowired
    private Oauth2Consent oauth2Consent;

    /**
     * Saves the {@link OAuth2AuthorizationConsent}.
     *
     * @param authorizationConsent the {@link OAuth2AuthorizationConsent}
     */
    @Override
    public void save(OAuth2AuthorizationConsent authorizationConsent) {
        oauth2Consent.save(OAuth2AuthorizationConsentCassandra.fromOauth2Consents(authorizationConsent));
    }

    /**
     * Removes the {@link OAuth2AuthorizationConsent}.
     *
     * @param authorizationConsent the {@link OAuth2AuthorizationConsent}
     */
    @Override
    public void remove(OAuth2AuthorizationConsent authorizationConsent) {
        oauth2Consent.deleteById(authorizationConsent.getRegisteredClientId());
    }

    /**
     * Returns the {@link OAuth2AuthorizationConsent} identified by the provided
     * {@code registeredClientId} and {@code principalName}, or {@code null} if not found.
     *
     * @param registeredClientId the identifier for the {@link RegisteredClient}
     * @param principalName      the name of the {@link Principal}
     * @return the {@link OAuth2AuthorizationConsent} if found, otherwise {@code null}
     */
    @Override
    public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
        Optional<OAuth2AuthorizationConsentCassandra> byId = oauth2Consent.findById(registeredClientId, principalName);
        return byId.map(OAuth2AuthorizationConsentCassandra::toOauth2Consent).orElse(null);
    }
}
