package org.local.meeting.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthTokens {
    String access_token;
    String refresh_token;
}
