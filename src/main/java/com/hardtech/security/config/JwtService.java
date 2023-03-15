package com.hardtech.security.config;

import com.hardtech.security.auth.requests.AuthenticationResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.hardtech.security.config.JWTUtils.SECRET_KEY;

@Service
@RequiredArgsConstructor
public class JwtService {

    public String extractUserEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public AuthenticationResponse generateTokens(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    //method that will help me to generate a token
    public AuthenticationResponse generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {


        String access_token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWTUtils.EXPIRATION_ACCESS_TOKEN))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        String refresh_token = Jwts
                .builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWTUtils.EXPIRATION_REFRESH_TOKEN))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        return AuthenticationResponse.builder()
                .access_token(access_token)
                .refresh_token(refresh_token)
                .build();


    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userEmail = extractUserEmail(token);
        return (userEmail.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //method which will allow me or which can extract a single claim that we pass
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
