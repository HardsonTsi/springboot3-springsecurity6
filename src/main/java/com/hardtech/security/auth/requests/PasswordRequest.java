package com.hardtech.security.auth.requests;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

public class PasswordRequest {
    String oldPassword;
    String newPassword;
}
