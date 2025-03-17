package org.local.meeting.Controllers;

import lombok.RequiredArgsConstructor;

import org.local.meeting.Models.AuthRequest;
import org.local.meeting.Models.AuthTokens;
import org.local.meeting.Models.PreRegRequest;
import org.local.meeting.Models.RegRequest;
import org.local.meeting.Services.AuthAndRegistrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class Authentication {

    private final AuthAndRegistrService authAndRegistrService;

    @GetMapping("/update_access_token")
    public ResponseEntity<?> refreshAuth(@RequestBody AuthTokens authTokens) {
        AuthTokens updateTokens = authAndRegistrService.refreshTokens(authTokens);
        return ResponseEntity.ok(updateTokens);
    }

    @GetMapping("/sign_in")
    public ResponseEntity<?> auth_and_authorize(AuthRequest authRequest) {
        return ResponseEntity.ok(authAndRegistrService.entry(authRequest));
    }

    @GetMapping("/sign_up")
    public ResponseEntity<?> register_and_auth(@RequestBody RegRequest auth) throws Exception {
        AuthTokens authTokens = authAndRegistrService.registrationOfUser(auth);
        return authTokens != null ? ResponseEntity.ok().body(authTokens) : ResponseEntity.badRequest().build();
    }

    @PostMapping("pre_sign_up")
    public ResponseEntity<?> pre_sign_up(@RequestBody PreRegRequest preRegRequest) {
        return authAndRegistrService.sendVerificationCode(preRegRequest)
                ? ResponseEntity.ok("code has been sent") :
                ResponseEntity.badRequest().build();
    }
}
