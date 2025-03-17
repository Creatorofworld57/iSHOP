package org.local.meeting.Models;


public class RegRequest extends AuthRequest {
    RegRequest(String login, String password) {
        super(login, password);
    }
    String email;
}
