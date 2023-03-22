package org.repoapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.repoapi.model.AppUser;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtConverter {
    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String ISSUER = "repo-api";
    private final int Expiration_MINUTES = 600;
    private final int EXPIRATION_MILLIS = Expiration_MINUTES * 60 * 1000;

    public String getTokenFromUser(AppUser appUser){
        String authorities = appUser.getAuthorities().stream()
                .map(i -> i.getAuthority())
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(appUser.getEmail())
                .claim("id", appUser.getId())
                .claim("authorities", authorities)
                .claim("firstName", appUser.getFirstName())
                .claim("lastName", appUser.getLastName())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MILLIS))
                .signWith(key)
                .compact();
    }

    public AppUser getUserFromToken(String token){
        if(token == null || !token.startsWith("Bearer ")) return null;
        try{
            Jws<Claims> jws = Jwts.parserBuilder()
                    .requireIssuer(ISSUER)
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token.substring(7));
            String email = jws.getBody().getSubject();
            String authStr = (String) jws.getBody().get("authorities");
            List<String> authorities = List.of(authStr.split(","));

            long appUserId = (long) jws.getBody().get("appUserId");
            String firstName = (String) jws.getBody().get("firstName");
            String lastName = (String) jws.getBody().get("lastName");

            return new AppUser(appUserId, email, email, firstName,lastName, false, authorities, null);
        }catch(JwtException e){
            e.printStackTrace();
        }
        return null;
    }
}
