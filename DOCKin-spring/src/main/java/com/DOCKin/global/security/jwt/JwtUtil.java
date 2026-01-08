package com.DOCKin.global.security.jwt;

import com.DOCKin.dto.CustomUserInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtil(
            @Value("${jwt.secret}") final String secretKey,
            @Value("${jwt.expiration_time}") final long accessTokenExpTime)
    {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key= Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime=accessTokenExpTime;
    }

    //Access Token 생성
    public String createAccessToken(CustomUserInfoDto member){
        return createToken(member, accessTokenExpTime);
    }

    //Jwt 생성
    private String createToken(CustomUserInfoDto member, long expireTime){
        Claims claims = Jwts.claims();
        claims.put("userId",member.getUserId());
        claims.put("name",member.getName());
        claims.put("role",member.getRole());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Token에서 UserId 추출
    public Long getUserId(String token){
        return parseClaims(token).get("userId",Long.class);
    }

    //jWT 검증
    public boolean isValidToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty", e);
        }
        return false;
    }

    //Jwt Cliams 추출
    public Claims parseClaims(String accessToken){
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch(ExpiredJwtException e){
            return e.getClaims();
        }
    }
}