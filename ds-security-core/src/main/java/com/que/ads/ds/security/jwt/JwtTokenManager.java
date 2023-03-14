package com.que.ads.ds.security.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.que.ads.ds.security.manager.KeyManager;
import com.que.ads.ds.security.models.security.SecurityUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenManager implements Serializable {
    private static final long serialVersionUID = 7008375124389347049L;
    public static final long TOKEN_VALIDITY = 5 * 60;

    @Value("${com.que.ads.jwt.token.issuer}")
    private String tokenIssuer;

    private final KeyManager keyManager;

    public JwtTokenManager(KeyManager keyManager) {
        this.keyManager = keyManager;
    }

    public String generate(SecurityUser userDetails) {
        try {
            keyManager.generateKeyPair();

            JWSSigner signer = new RSASSASigner(keyManager.getRsaKey());

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(userDetails.getUsername())
                    .claim("authorities", userDetails.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList()))
                    .claim("userId", userDetails.getUser().getId())
                    .issuer(tokenIssuer)
                    .expirationTime(new Date(new Date().getTime() + TOKEN_VALIDITY * 1000))
                    .build();
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyManager.getRsaKey().getKeyID()).build(),
                    claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException ex) {
            return null;
        }
    }

    public Boolean validate(String token) {
        try {
            var signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(keyManager.getRsaKey().toPublicJWK());

            if (!signedJWT.verify(verifier)) {
                return false;
            }

            if (!tokenIssuer.equalsIgnoreCase(signedJWT.getJWTClaimsSet().getIssuer())) {
                return false;
            }

            return (new Date()).before(signedJWT.getJWTClaimsSet().getExpirationTime());

        } catch (JOSEException ex) {
            return false;
        } catch (ParseException ex) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            var signedJWT = SignedJWT.parse(token);

            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException ex) {
            return null;
        }
    }

    public String getClaim(String token, String claimName) {
        try {
            var signedJWT = SignedJWT.parse(token);

            return signedJWT.getJWTClaimsSet().getClaim(claimName).toString();
        } catch (ParseException ex) {
            return null;
        }
    }

    public List<String> getAuthorities(String token) {
        try {
            var signedJWT = SignedJWT.parse(token);

            return (List<String>) signedJWT.getJWTClaimsSet().getClaim("authorities");
        } catch (ParseException ex) {
            return null;
        }
    }

}