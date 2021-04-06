package by.bsuir.spp.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface JwtTokenProvider {

    String createJwtToken(String username);
    boolean validateJwtToken(String token) throws AuthenticationServiceException;
    Authentication getAuthentication(String token) throws AuthenticationServiceException;
    String getUserName(String token) throws AuthenticationServiceException;
    String resolveToken(HttpServletRequest request) throws AuthenticationServiceException;
}
