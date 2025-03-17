package org.local.meeting.Services;

import lombok.RequiredArgsConstructor;
import org.local.meeting.Exeptions.UserAlreadyExistsException;
import org.local.meeting.Models.*;
import org.local.meeting.Repositories.UserRepository;
import org.local.meeting.Utils.JwtUtils.JwtTokenUtil;
import org.local.meeting.Utils.JwtUtils.UserVerifyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthAndRegistrService {
    private final UserVerifyService userVerifyService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public boolean sendVerificationCode(PreRegRequest preRegRequest) {
        return false;
    }


    public AuthTokens registrationOfUser(RegRequest regRequest) throws Exception {
        if (userRepository.findByName(regRequest.getLogin()) != null) {
            throw new UserAlreadyExistsException("User with login '" + regRequest.getLogin() + "' already exists");
        }
        UserA user = new UserA();
        user.setName(regRequest.getLogin());
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        userRepository.save(user);

        return new AuthTokens(
                userVerifyService.authenticateAndGetToken(
                        new AuthRequest(regRequest.getLogin(), regRequest.getPassword())),
                jwtTokenUtil.generateRefreshToken(regRequest.getLogin())

        );
    }

    public AuthTokens refreshTokens(AuthTokens authTokens) {
        return null;
    }

    public AuthTokens entry(AuthRequest authRequest) {
        return null;
    }

}
