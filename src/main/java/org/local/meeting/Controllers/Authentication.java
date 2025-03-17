package org.local.meeting.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Authentication {

    @GetMapping("/validate_auth")
    public String validateAuth() {
        return "Hello World";
    }
    @GetMapping("/login")
    public String auth_and_author() {
        return "Hello World";
    }



}
