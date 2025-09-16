package hr.zavrsni.trainflowspringbackend.authServices;


import hr.zavrsni.trainflowspringbackend.authDomain.RefreshToken;
import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import hr.zavrsni.trainflowspringbackend.authRepositories.RefreshTokenRepository;
import hr.zavrsni.trainflowspringbackend.authRepositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.refresh-token-validity-ms}")
    private Long refreshTokenValidityMs;

    @Transactional
    public RefreshToken createRefreshToken(String email){
        UserInfo user = userRepository.findByEmail(email);
        refreshTokenRepository.deleteByUserId(user.getId());

        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByEmail(email))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenValidityMs))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteRefreshToken(RefreshToken token){
        refreshTokenRepository.delete(token);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;
    }

}