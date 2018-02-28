package com.rawlead.github.entity;

public interface ResponseMessage {
    String DUPLICATE_USER = "User already exists";
    String PASSWORD_MISMATCH = "Passwords don't match";
    String WRONG_CREDENTIALS = "Wrong username or password";
    String EMPTY_FIELD = "Please fill all the fields";
    String SPECIAL_CHARACTERS = "Username contains special characters";
    String INVALID_EMAIL = "Please enter valid email address";
    String EMAIL_MISMATCH = "Emails don't match";
    String EMAIL_UPDATED = "Email has been changed";
    String NEW_PASSWORDS_MISMATCH = "Password does not match the confirm password";
    String PASSWORD_UPDATED = "Password has been changed";
    String CURRENT_PASSWORD_MISMATCH = "Please enter the correct current password";
    String AVATAR_CHANGED = "Profile picture has been changed";
    String USER_NOT_FOUND = "Author not found";
}