package com.finalproject.hwangjunha_team3.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenUtil {

    public static Boolean validate(String token, String userName, String key) {
        String usernameByToken = getUsername(token, key);
        return usernameByToken.equals(userName) && !isExpired(token, key);
    }
    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public static String getUsername(String token, String key) {
        return extractClaims(token, key).get("username", String.class);
    }
    public static boolean isExpired(String token, String secretkey) {
        // expire timestamp를 return함
        Date expiredDate = extractClaims(token, secretkey).getExpiration();
        return expiredDate.before(new Date());
    }
    public static String getUserName(String token, String secretkey) {
        return extractClaims(token, secretkey).get("username").toString();
    }

    public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims(); // 일종의 map
        claims.put("username", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact()
                ;
    }
}