package org.local.meeting.Models.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegRequest extends AuthRequest {
    RegRequest(String login, String password) {
        super(login, password);
    }
    String email;
}
