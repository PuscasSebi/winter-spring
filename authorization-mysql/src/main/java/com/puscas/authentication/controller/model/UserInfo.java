package com.puscas.authentication.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public class UserInfo {
    String name;
    String email;
    String password;
    String confirmPassword;
}
