package com.rawlead.github.service;

import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
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
        String url = amazonClient.uploadFile(avatarImage);
        deleteUserAvatar(userId);
        user.setAvatarUrl(url);
        userRepository.save(user);
    }

    public void createNewUser(UserRegistrationForm userRegistrationForm) {
        User user = new User();
        user.setFirstName(userRegistrationForm.getFirstName());
        user.setLastName(userRegistrationForm.getLastName());
        user.setEmail(userRegistrationForm.getEmail());
        user.setPassword(getPasswordEncoder().encode(userRegistrationForm.getPassword()));
        user.setUsername(userRegistrationForm.getUsername());
        user.setRoles(Arrays.asList(new Role("USER"),new Role("PHOTOGRAPHER")));
        user.setAvatarUrl("/img/user-icon-white.png");
        userRepository.save(user);
    }

    public void updateUserEmail(Long userId, String newEmail, String newEmailConfirm) {
        User user = userRepository.getOne(userId);
        user.setEmail(newEmail);
        userRepository.save(user);
    }

    public boolean updateUserPassword(Long userId, String oldPass, String newPass, String newPassConfirm) {
        User user = userRepository.getOne(userId);
        if(!getPasswordEncoder().matches(oldPass,user.getPassword()))
            return false;

        user.setPassword(getPasswordEncoder().encode(newPass));
        userRepository.save(user);
        return true;
    }
}
