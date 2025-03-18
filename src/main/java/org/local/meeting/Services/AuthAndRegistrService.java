package org.local.meeting.Services;

import lombok.RequiredArgsConstructor;
import org.local.meeting.Exeptions.UserAlreadyExistsException;
import org.local.meeting.Models.Dao.UserA;
import org.local.meeting.Models.Dto.AuthRequest;
import org.local.meeting.Models.Dto.AuthTokens;
import org.local.meeting.Models.Dto.PreRegRequest;
import org.local.meeting.Models.Dto.RegRequest;
import org.local.meeting.Repositories.UserRepository;
import org.local.meeting.Services.KafkaServices.ProducerOfPassCodes;
import org.local.meeting.Utils.JwtUtils.JwtTokenUtil;
import org.local.meeting.Utils.JwtUtils.UserVerifyService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthAndRegistrService {
    private final UserVerifyService userVerifyService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final ProducerOfPassCodes producerOfPassCodes;
    private final PassCodeService passCodeService;

    public boolean sendVerificationCode(String email) {
        producerOfPassCodes.send(email,
                passCodeService.create_code(email));
        return true; //добавить исключение
    }


    public AuthTokens registrationOfUser(RegRequest regRequest) throws Exception {
        if (userRepository.findByLogin(regRequest.getLogin()) != null) {
            throw new UserAlreadyExistsException("User with login '" + regRequest.getLogin() + "' already exists");
        }
        UserA user = new UserA();
        user.setLogin(regRequest.getLogin());
        user.setEmail(regRequest.getEmail());
        user.setPassword(passwordEncoder.encode(regRequest.getPassword()));
        userRepository.save(user);

        return new AuthTokens(
                userVerifyService.authenticateAndGetToken(
                        new AuthRequest(regRequest.getLogin(), regRequest.getPassword())),
                jwtTokenUtil.generateRefreshToken(regRequest.getLogin())

        );
    }

    public AuthTokens refreshTokens(AuthTokens authTokens) {
        UserA user = userRepository.findByLogin(jwtTokenUtil.extractUserName(authTokens.getAccess_token()));
        return new AuthTokens(
                userVerifyService.authenticateAndGetToken(
                        new AuthRequest(user.getLogin(), user.getPassword())),
                authTokens.getRefresh_token()
        );
    }

    public AuthTokens entry(AuthRequest authRequest) throws Exception {
        return new AuthTokens(
                userVerifyService.authenticateAndGetToken(
                        new AuthRequest(authRequest.getLogin(), authRequest.getPassword())),
                jwtTokenUtil.generateRefreshToken(authRequest.getLogin())

        );
    }

}
