package com.rawlead.github.pojo;

// ...
// Interfejs z niestandardowymi komunikatami
// dla opisu odpowiedzi.
public interface ResponseMessage {
    String PASSWORD_SIZE = "Password must be at least 6 characters";
    String DUPLICATE_USER = "User already exists";
    String PHOTO_PUBLISHED = "Photo has been published";
    // ...
    String PASSWORD_MISMATCH = "Passwords don't match";
    String WRONG_CREDENTIALS = "Wrong username or password";
    String EMPTY_FIELD = "Please fill all fields";
    String SPECIAL_CHARACTERS = "Username contains special characters";
    String INVALID_EMAIL = "Please enter valid email address";
    String EMAIL_MISMATCH = "Emails don't match";
    String EMAIL_UPDATED = "Email has been changed";
    String NEW_PASSWORDS_MISMATCH = "Password does not match the confirm password";
    String PASSWORD_UPDATED = "Password has been changed";
    String CURRENT_PASSWORD_MISMATCH = "Please enter the correct current password";
    String AVATAR_CHANGED = "Profile picture has been changed";
    String USER_NOT_FOUND = "User not found";
    String PHOTO_DELETED = "Photo has been deleted";
    String USER_ALREADY_FAVORITE_OR_DOESNT_EXIST = "User already in favorites or doesn't exist";

    String FAVORITE_MYSELF = "Cannot add or delete myself to or from favorite users";
    String FAVORITE_OWN_PHOTO = "Cannot add or delete own photo to or from favorite photos";
    String PHOTO_ALREADY_FAVORITE_OR_DOESNT_EXIST = "Photo already in favorites or doesn't exist";
    String USERNAME_SIZE = "Username should be at least 2 characters";
    String PHOTO_NOT_FOUND = "Photo not found";

    String PHOTO_FAVORITE_ADD = "Photo has been added to favorites";
    String PHOTO_FAVORITE_REMOVE = "Photo has been removed from favorites";

    String USER_FAVORITE_ADD = "User has been added to favorites";
    String USER_FAVORITE_REMOVE = "User has been removed from favorites";

}