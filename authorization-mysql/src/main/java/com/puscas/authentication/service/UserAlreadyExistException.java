package com.puscas.authentication.service;

import lombok.NonNull;

import javax.validation.constraints.NotEmpty;

public class UserAlreadyExistException extends Throwable {
    public UserAlreadyExistException(@NonNull @NotEmpty String message) {
    }
}
