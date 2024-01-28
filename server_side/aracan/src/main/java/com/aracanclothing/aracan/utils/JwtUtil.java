package com.aracanclothing.aracan.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtUtil {


    public static final String JWTSecret = "/mmCe2NpvVJSXZg0N9GibzjrpK139U3X21w1md9ER80=";

    public String generateToken(String user_name) {
        HashMap<String, Object> jwt_claims = new HashMap<>();
        return createToken(jwt_claims, user_name);
    }

    private String createToken(HashMap<String, Object> jwt_claims, String user_name) {
        return Jwts.builder()
                .claims(jwt_claims)
                .subject(user_name)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10000 * 60 * 30))
                .signWith(getSignKey()).compact();
    }

    private Key getSignKey() {
        byte[] key_bytes = Decoders.BASE64.decode(JWTSecret);
        return Keys.hmacShaKeyFor(key_bytes);
    }

    public String retrieveUsername(String token) {
        return retrieveClaim(token, Claims::getSubject);
    }

    public <T> T retrieveClaim(String token, Function<Claims, T> claims_resolver) {
        final Claims claims = retrieveAllClaims(token);
        return claims_resolver.apply(claims);
    }

    private Claims retrieveAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return retrieveExpiration(token).before(new Date());
    }

    public Date retrieveExpiration(String token) {
        return retrieveClaim(token, Claims::getExpiration);
    }

    public Boolean validToken(String token, UserDetails user_details) {
        final String user_name = retrieveUsername(token);
        return (user_name.equals(user_details.getUsername()) && !isTokenExpired(token));
    }


/**

    Code used to generate JWTSecret:

    static int secretLengthBytes = 32; // Length of bytes array
    static byte[] secretBytes = generateRandomBytes(secretLengthBytes); // Array of

    Base 10 = Decimal, Base 2 = Binary, Base 16 = Hexadecimal, Base 64 = A-Z, a-Z, 0-9, +, /
    public static final String secret = Base64.getEncoder().encodeToString(secretBytes);

    public static void main(String[] args) {
        System.out.println(secret);
    }


    private static byte[] generateRandomBytes(int length) {
        SecureRandom secureRandom = new SecureRandom(); // Class used for cryptography
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes); //fills array with random bytes
        return bytes;
    }

 **/

}
