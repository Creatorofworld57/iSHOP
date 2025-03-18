package org.local.meeting.Controllers;

import lombok.RequiredArgsConstructor;

import org.local.meeting.Models.Dao.UserA;
import org.local.meeting.Models.Dto.*;
import org.local.meeting.Repositories.UserRepository;
import org.local.meeting.Services.AuthAndRegistrService;
import org.local.meeting.Services.PassCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Authentication {

    private final AuthAndRegistrService authAndRegistrService;
    private final PassCodeService passCodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/update_access_token")
    public ResponseEntity<?> refreshAuth(@RequestBody AuthTokens authTokens) {
        AuthTokens updateTokens = authAndRegistrService.refreshTokens(authTokens);
        return ResponseEntity.ok(updateTokens);
    }

    @PostMapping("/sign_in")
    public ResponseEntity<?> auth_and_authorize(@RequestBody AuthRequest authRequest)  {
        UserA user = userRepository.findByLogin(authRequest.getLogin());
        if(passwordEncoder.matches(authRequest.getPassword(), user.getPassword().substring(6))) {
            return authAndRegistrService.sendVerificationCode(user.getEmail())
                    ? ResponseEntity.ok("code has been sent") :
                    ResponseEntity.badRequest().build();
        }
       else
           return ResponseEntity.status(401).body("Invalid login or password");
    }

    @GetMapping("/sign_up")
    public ResponseEntity<?> register_and_auth(@RequestBody RegRequest auth) throws Exception {
        AuthTokens authTokens = authAndRegistrService.registrationOfUser(auth);
        return authTokens != null ? ResponseEntity.ok().body(authTokens) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/pre_sign_up")
    public ResponseEntity<?> pre_sign_up(@RequestBody PreRegRequest preRegRequest) {
        return authAndRegistrService.sendVerificationCode(preRegRequest.getEmail())
                ? ResponseEntity.ok("code has been sent") :
                ResponseEntity.badRequest().build();
    }

    @GetMapping("/check_code")
    public ResponseEntity<?> after_true_code(@RequestBody PassCode passCode) throws Exception {
        if (passCodeService.verify_code(passCode.getCode(), passCode.getEmail()))
            return ResponseEntity.ok().body(
                    authAndRegistrService.entry(
                            userRepository.findByEmail(passCode.getEmail())));
        else
            return ResponseEntity.badRequest().body("Pass code is wrong");
    }
    // сделать endpoint для обновления refresh токена
}
