package com.rawlead.github.service;

import com.rawlead.github.entity.User;
import com.rawlead.github.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserService {
    private UserRepository userRepository;
    private AmazonClient amazonClient;

    @Autowired
    public UserService(UserRepository userRepository, AmazonClient amazonClient) {
        this.userRepository = userRepository;
        this.amazonClient = amazonClient;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void save(User user) {
//        user.setPassword(getPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }



    public boolean deleteUserAvatar(Long userId) {
        User user = userRepository.findOne(userId);
        String avatarUrl = user.getAvatarUrl();
        if(avatarUrl.equals(""))
            return false;
        amazonClient.deleteFileFromS3Bucket(avatarUrl);
        user.setAvatarUrl("");
        userRepository.save(user);
        return true;
    }

    public void updateUserAvatar(Long userId, MultipartFile avatarImage) {
        User user = userRepository.findOne(userId);
        deleteUserAvatar(userId);
        String url = amazonClient.uploadFile(avatarImage);
        user.setAvatarUrl(url);
        userRepository.save(user);
    }
}
