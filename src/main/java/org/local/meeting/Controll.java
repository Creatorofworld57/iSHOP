package org.local.meeting;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Операции с пользователями", description = "Управление пользователями")
@RestController
public class Controll {

    @Operation(summary = "deffffffff")
    @GetMapping("/hello")
    public String api() {
        return "Hello World";
    }
}
