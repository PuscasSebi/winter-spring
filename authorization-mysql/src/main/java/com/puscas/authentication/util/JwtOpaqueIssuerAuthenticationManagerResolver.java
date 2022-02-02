/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.puscas.authentication.util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;

import com.nimbusds.jwt.JWTParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.log.LogMessage;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * An implementation of {@link AuthenticationManagerResolver} that resolves a JWT-based
 * {@link AuthenticationManager} based on the <a href=
 * "https://openid.net/specs/openid-connect-core-1_0.html#IssuerIdentifier">Issuer</a> in
 * a signed JWT (JWS).
 *
 * To use, this class must be able to determine whether or not the `iss` claim is trusted.
 * Recall that anyone can stand up an authorization server and issue valid tokens to a
 * resource server. The simplest way to achieve this is to supply a list of trusted
 * issuers in the constructor.
 *
 * This class derives the Issuer from the `iss` claim found in the
 * {@link HttpServletRequest}'s
 * <a href="https://tools.ietf.org/html/rfc6750#section-1.2" target="_blank">Bearer
 * Token</a>.
 *
 * @author Josh Cummings
 * @since 5.3
 */
public final class JwtOpaqueIssuerAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

	private final AuthenticationManager authenticationManager;

	/**
	 * Construct a {@link JwtOpaqueIssuerAuthenticationManagerResolver} using the provided
	 * parameters
	 * @param trustedIssuers a list of trusted issuers
	 */
	public JwtOpaqueIssuerAuthenticationManagerResolver(String... trustedIssuers) {
		this(Arrays.asList(trustedIssuers));
	}

	/**
	 * Construct a {@link JwtOpaqueIssuerAuthenticationManagerResolver} using the provided
	 * parameters
	 * @param trustedIssuers a list of trusted issuers
	 */
	public JwtOpaqueIssuerAuthenticationManagerResolver(Collection<String> trustedIssuers) {
		Assert.notEmpty(trustedIssuers, "trustedIssuers cannot be empty");
		this.authenticationManager = new ResolvingAuthenticationManager(
				new TrustedIssuerJwtAuthenticationManagerResolver(
						Collections.unmodifiableCollection(trustedIssuers)::contains));
	}

	/**
	 * Construct a {@link JwtOpaqueIssuerAuthenticationManagerResolver} using the provided
	 * parameters
	 *
	 * Note that the {@link AuthenticationManagerResolver} provided in this constructor
	 * will need to verify that the issuer is trusted. This should be done via an
	 * allowlist.
	 *
	 * One way to achieve this is with a {@link Map} where the keys are the known issuers:
	 * <pre>
	 *     Map&lt;String, AuthenticationManager&gt; authenticationManagers = new HashMap&lt;&gt;();
	 *     authenticationManagers.put("https://issuerOne.example.org", managerOne);
	 *     authenticationManagers.put("https://issuerTwo.example.org", managerTwo);
	 *     JwtAuthenticationManagerResolver resolver = new JwtAuthenticationManagerResolver
	 *     	(authenticationManagers::get);
	 * </pre>
	 *
	 * The keys in the {@link Map} are the allowed issuers.
	 * @param issuerAuthenticationManagerResolver a strategy for resolving the
	 * {@link AuthenticationManager} by the issuer
	 */
	public JwtOpaqueIssuerAuthenticationManagerResolver(
			AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver) {
		Assert.notNull(issuerAuthenticationManagerResolver, "issuerAuthenticationManagerResolver cannot be null");
		this.authenticationManager = new ResolvingAuthenticationManager(issuerAuthenticationManagerResolver);
	}

	/**
	 * Return an {@link AuthenticationManager} based off of the `iss` claim found in the
	 * request's bearer token
	 * @throws OAuth2AuthenticationException if the bearer token is malformed or an
	 * {@link AuthenticationManager} can't be derived from the issuer
	 */
	@Override
	public AuthenticationManager resolve(HttpServletRequest request) {
		return this.authenticationManager;
	}

	private static class ResolvingAuthenticationManager implements AuthenticationManager {

		private final Converter<BearerTokenAuthenticationToken, String> issuerConverter = new JwtClaimIssuerConverter();

		private final AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver;

        private final RestTemplate restTemplate = new RestTemplate();

		ResolvingAuthenticationManager(AuthenticationManagerResolver<String> issuerAuthenticationManagerResolver) {
			this.issuerAuthenticationManagerResolver = issuerAuthenticationManagerResolver;
		}

		@Override
		public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            OpaqueTokenIntrospector opaqueTokenIntrospector = new SpringOpaqueTokenIntrospector(
                    "https://www.googleapis.com/oauth2/v1/tokeninfo",
                    "246861652401-bhs3d3435gof3dfggfkl6qk1190qpj9q.apps.googleusercontent.com",
                    "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"

            );
			Assert.isTrue(authentication instanceof BearerTokenAuthenticationToken,
					"Authentication must be of type BearerTokenAuthenticationToken");
			BearerTokenAuthenticationToken token = (BearerTokenAuthenticationToken) authentication;
			try {
				String issuer = this.issuerConverter.convert(token);
				AuthenticationManager authenticationManager = this.issuerAuthenticationManagerResolver.resolve(issuer);
				if (authenticationManager == null) {
					throw new InvalidBearerTokenException("Invalid issuer");
				}
				return authenticationManager.authenticate(authentication);
			}catch (Exception ex){
				try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.add("PRIVATE-TOKEN", "xyz");

                    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                    map.add("access_token", token.getToken());

                    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);


					Optional<GoogleAuthentication> body = getGoogleAuthentication(entity);
					if(body.isPresent()){
						return body.get();
					}

				}catch (Exception e){
					System.out.println(e);
                    throw e;
				}
			}
			return null;
		}

		private Optional<GoogleAuthentication> getGoogleAuthentication(HttpEntity<MultiValueMap<String, String>> entity) {
			try {
				ResponseEntity<Map> exchange = restTemplate.exchange("https://www.googleapis.com/oauth2/v1/tokeninfo",
						HttpMethod.POST, entity, Map.class);

				if (exchange.getStatusCode() == HttpStatus.OK) {
					Map<String, Object> body = exchange.getBody();
					Integer expires_in = (Integer) body.get("expires_in");

					Boolean verified_email = (Boolean) body.get("verified_email");
					if (expires_in> 0
							&& verified_email) {
						return Optional.of(new GoogleAuthentication(body));
					}
				}
			}catch (Exception ex){
				System.out.println(ex);
			}
			return Optional.empty();
		}

	}

	private static class JwtClaimIssuerConverter implements Converter<BearerTokenAuthenticationToken, String> {

		@Override
		public String convert(@NonNull BearerTokenAuthenticationToken authentication) {
			String token = authentication.getToken();
			try {
				String issuer = JWTParser.parse(token).getJWTClaimsSet().getIssuer();
				if (issuer != null) {
					return issuer;
				}
			}
			catch (Exception ex) {
				throw new InvalidBearerTokenException(ex.getMessage(), ex);
			}
			throw new InvalidBearerTokenException("Missing issuer");
		}

	}

	static class TrustedIssuerJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {

		private final Log logger = LogFactory.getLog(getClass());

		private final Map<String, AuthenticationManager> authenticationManagers = new ConcurrentHashMap<>();

		private final Predicate<String> trustedIssuer;

		TrustedIssuerJwtAuthenticationManagerResolver(Predicate<String> trustedIssuer) {
			this.trustedIssuer = trustedIssuer;
		}

		@Override
		public AuthenticationManager resolve(String issuer) {
			if (this.trustedIssuer.test(issuer)) {
				AuthenticationManager authenticationManager = this.authenticationManagers.computeIfAbsent(issuer,
						(k) -> {
							this.logger.debug("Constructing AuthenticationManager");
							JwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(issuer);
							return new JwtAuthenticationProvider(jwtDecoder)::authenticate;
						});
				this.logger.debug(LogMessage.format("Resolved AuthenticationManager for issuer '%s'", issuer));
				return authenticationManager;
			}
			else {
				this.logger.debug("Did not resolve AuthenticationManager since issuer is not trusted");
			}
			return null;
		}

	}

}
