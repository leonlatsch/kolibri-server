package de.leonlatsch.oliviabackend.service;

import de.leonlatsch.oliviabackend.entity.AccessToken;
import de.leonlatsch.oliviabackend.repository.AccessTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessTokenService {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public String getTokenForUser(int uid) {
        Optional<AccessToken> token = accessTokenRepository.findByUidAndValidTrue(uid);

        return token.isPresent() ? token.get().getToken() : null;
    }

    public int getUserForToken(String token) {
        Optional<AccessToken> accessToken = accessTokenRepository.findById(token);

        return accessToken.isPresent() && accessToken.get().isValid() ? accessToken.get().getUid() : -1;
    }

    public void disableAccessToken(String accessToken) {
        Optional<AccessToken> token = accessTokenRepository.findById(accessToken);
        if (token.isPresent()) {
            token.get().setValid(false);
            accessTokenRepository.saveAndFlush(token.get());
        }
    }
}
