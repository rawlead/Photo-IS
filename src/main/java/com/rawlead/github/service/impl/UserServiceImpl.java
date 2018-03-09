package com.rawlead.github.service.impl;

import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.AmazonClientService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AmazonClientService amazonClientService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AmazonClientService amazonClientService) {
        this.userRepository = userRepository;
        this.amazonClientService = amazonClientService;
    }

    @Override
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findOne(id);
    }

    @Override
    public boolean deleteUserAvatar(Long userId) {
        User user = userRepository.findOne(userId);
        String avatarUrl = user.getAvatarUrl();
        if (avatarUrl.equals(""))
            return false;
        amazonClientService.deleteFileFromS3Bucket(avatarUrl);
        user.setAvatarUrl("");
        userRepository.save(user);
        return true;
    }

    @Override
    public User updateUserAvatar(Long userId, MultipartFile avatarImage) {
        User user = userRepository.findOne(userId);
        String url = amazonClientService.uploadFile(avatarImage);
        deleteUserAvatar(userId);
        user.setAvatarUrl(url);
        return userRepository.save(user);
    }

    @Override
    public User save(UserRegistrationForm userRegistrationForm) {
        User user = new User();
        user.setFirstName(userRegistrationForm.getFirstName());
        user.setLastName(userRegistrationForm.getLastName());
        user.setEmail(userRegistrationForm.getEmail());
        user.setPassword(getPasswordEncoder().encode(userRegistrationForm.getPassword()));
        user.setUsername(userRegistrationForm.getUsername());
        user.setRoles(Arrays.asList(new Role("USER"), new Role("PHOTOGRAPHER")));
        user.setAvatarUrl("/img/user-icon-white.png");
        return userRepository.save(user);
    }

    @Override
    public User updateUserEmail(Long userId, String newEmail, String newEmailConfirm) {
        User user = userRepository.getOne(userId);
        user.setEmail(newEmail);
        return userRepository.save(user);
    }

    @Override
    public boolean updateUserPassword(Long userId, String oldPass, String newPass, String newPassConfirm) {
        User user = userRepository.getOne(userId);
        if (!getPasswordEncoder().matches(oldPass, user.getPassword()))
            return false;
        user.setPassword(getPasswordEncoder().encode(newPass));
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> listFavoriteUsers(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            return new ArrayList<>();
        return user.getFavoriteUsers();
    }

    @Override
    public boolean addFavoriteUser(Long userId, Long favoriteUserId) {
        User user = userRepository.findOne(userId);
        User favoriteUser = userRepository.findOne(favoriteUserId);
        if (user == null || favoriteUser == null)
            return false;
        if (!user.addFavoriteUser(favoriteUser))
            return false;
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean deleteFavoriteUser(Long userId, Long favoriteUserId) {
        User user = userRepository.findOne(userId);
        User favoriteUser = userRepository.findOne(favoriteUserId);
        if (user == null || favoriteUser == null)
            return false;
        if (!user.deleteFavoriteUser(favoriteUser))
            return false;
        userRepository.save(user);
        return true;
    }

    @Override
    public User getFavoriteUser(Long userId, Long favoriteUserId) {
        User user = userRepository.findOne(userId);
        User favoriteUser = userRepository.findOne(favoriteUserId);
        if (user.hasFavoriteUser(favoriteUser))
            return favoriteUser;
        return null;
    }
}























