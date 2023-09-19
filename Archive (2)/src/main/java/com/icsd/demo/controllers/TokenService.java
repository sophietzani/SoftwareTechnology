package com.icsd.demo.controllers;

import com.icsd.demo.models.TokenModel;
import com.icsd.demo.repositories.TokenRepository;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

//    public TokenService(TokenRepository tokenRepository) {
//        this.tokenRepository = tokenRepository;
//    }

    public TokenModel generateToken(String username, String password) {
        // Generate token code based on username, password, and current date/time
        String tokenCode = generateTokenCode(username, password, LocalDateTime.now());

        //create a new token to store information
        TokenModel token = new TokenModel();
        token.setCode(tokenCode);
        token.setPassword(password);
        token.setUsername(username);
        token.setCreationtime(LocalDateTime.now());
        //create a second localdatetime variable that is five hours later than now
        LocalDateTime newDateTime = LocalDateTime.now().plusHours(5);

        token.setExpiration(newDateTime);

        return tokenRepository.save(token);
    }
    

    public boolean validateToken(String code) {
        Optional<TokenModel> mtokenopt = tokenRepository.findByCode(code);
        
        // Check if the token exists and is not expired
        return mtokenopt
                .filter(token -> token.getExpiration().isAfter(LocalDateTime.now()))
                .isPresent();
    }

    private String generateTokenCode(String username, String password, LocalDateTime dateTime) {
        // Implement your custom hashing logic here
        // For simplicity, let's use SHA-256 hash
        String data = username + password + dateTime.toString();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available.", e);
        }
    }
}
