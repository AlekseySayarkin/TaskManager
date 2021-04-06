package by.bsuir.spp.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Log4j
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final int EXPIRATION_IN_MILLISECONDS = 3600000;

    private String secretKey = "secret";

    public JwtTokenProviderImpl(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createJwtToken(String username) {
        var claims = Jwts.claims().setSubject(username);
        var now = new Date();
        var expiration = new Date(now.getTime() + EXPIRATION_IN_MILLISECONDS);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public boolean validateJwtToken(String token) throws AuthenticationServiceException {
        try {
            var claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return new Date().before(claimsJws.getBody().getExpiration());
        } catch (ExpiredJwtException e) {
            log.error("Failed to validate jwt token: jwt token expired");
            throw new AuthenticationServiceException("Failed to validate jwt token: jwt token expired");
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to validate jwt token");
            throw new AuthenticationServiceException("Failed to validate jwt token");
        }
    }

    @Override
    public Authentication getAuthentication(String token) throws AuthenticationServiceException {
        var details = userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(
                details, null, Collections.singleton(new SimpleGrantedAuthority("any"))
        );
    }

    @Override
    public String getUserName(String token) throws AuthenticationServiceException {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Failed to get username from jwt token");
            throw new AuthenticationServiceException("Failed to get username from jwt token");
        }
    }

    @Override
    public String resolveToken(HttpServletRequest request) throws AuthenticationServiceException {
        var token = request.getHeader(AUTHORIZATION_HEADER);
        return token == null || token.isEmpty() ? null : token;
    }
}
