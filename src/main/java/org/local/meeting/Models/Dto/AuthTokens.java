package org.local.meeting.Models.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthTokens {
    String access_token;
    String refresh_token;
}
