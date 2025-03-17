package org.local.meeting.Utils.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.local.meeting.Exeptions.AuthException;
import org.local.meeting.Models.AuthRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserVerifyService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;


    public String authenticateAndGetToken(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getLogin(),
                            request.getPassword()
                    )
            );

            if (!authentication.isAuthenticated()) {
                log.warn("Authentication failed for user: {}", request.getLogin());
                throw new AuthException("Invalid credentials");
            }

            log.info("User authenticated successfully: {}", request.getLogin());
            return jwtTokenUtil.generateToken(request.getLogin());

        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            log.error("Authentication error for user: {}", request.getLogin(), e);
            throw new AuthException("Authentication failed", e);
        }
    }
}