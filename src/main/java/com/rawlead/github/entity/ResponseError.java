package com.rawlead.github.entity;

public interface ResponseError {
    String DUPLICATE_USER = "User already exists";
    String PASSWORD_MISMATCH = "Passwords don't match";
    String WRONG_CREDENTIALS = "Wrong username or password";
    String EMPTY_FIELD = "Please fill all the fields";
    String SPECIAL_CHARACTERS = "Username contains special characters";
    String INVALID_EMAIL = "Please enter valid email address";
}