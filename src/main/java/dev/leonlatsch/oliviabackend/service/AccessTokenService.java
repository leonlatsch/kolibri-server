package dev.leonlatsch.oliviabackend.service;

import dev.leonlatsch.oliviabackend.model.entity.AccessToken;
import dev.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to manage access tokens.
 *
 * @author Leon Latsch
 * @since 1.0.0
 */
@Service
public class AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public String getTokenForUser(String uid) {
        Optional<AccessToken> token = accessTokenRepository.findByUidAndValidTrue(uid);

        return token.isPresent() && token.get().isValid() ? token.get().getToken() : null;
    }

    public String getUserForToken(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findById(token);

        return accessToken.isPresent() && accessToken.get().isValid() ? accessToken.get().getUid() : null;
    }

    public void disableAccessToken(String accessToken) {
        Optional<AccessToken> token = accessTokenRepository.findById(accessToken);
        if (token.isPresent()) {
            token.get().setValid(false);
            accessTokenRepository.saveAndFlush(token.get());
        }
    }

    public AccessToken saveAccessToken(AccessToken accessToken) {
        if (accessToken.validate()) {
            return accessTokenRepository.saveAndFlush(accessToken);
        } else {
            return null;
        }
    }

    public boolean isTokenValid(String accessToken) {
        Optional<AccessToken> token = accessTokenRepository.findById(accessToken);
        return token.isPresent() && token.get().isValid();
    }
}
