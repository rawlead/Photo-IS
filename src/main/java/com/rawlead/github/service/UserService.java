package com.rawlead.github.service;

import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface UserService {
    @Bean
    PasswordEncoder getPasswordEncoder();

    User save (User user);

    User save(UserRegistrationForm userRegistrationForm);

    User getUser(String username);

    User getUser(Long id);

    List<User> listAllUsers();

    boolean deleteUserAvatar(Long userId);

    User updateUserAvatar(Long userId, MultipartFile avatarImage);

    User updateUserEmail(Long userId, String newEmail, String newEmailConfirm);

    boolean updateUserPassword(Long userId, String oldPass, String newPass, String newPassConfirm);

    Set<User> listFavoriteUsers(Long userId);

    boolean addFavoriteUser(Long userId, Long favoriteUserId);

    boolean deleteFavoriteUser(Long userId, Long favoriteUserId);

    User getFavoriteUser(Long userId, Long favoriteUserId);

    boolean deleteUser(User user);
    boolean deleteUser(Long userId);
}
