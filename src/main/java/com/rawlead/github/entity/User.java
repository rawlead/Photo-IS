package com.rawlead.github.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles;
    private String avatarUrl;
    private LocalDateTime registrationDate;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Photo> photos;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> favoriteUsers;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Photo> favoritePhotos;

    public User() {
        this.registrationDate = LocalDateTime.now();
    }

    public User(String firstName, String lastName, String email, String username, String password, List<Role> roles, String avatarUrl) {
        this.registrationDate = LocalDateTime.now();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Set<User> getFavoriteUsers() {
        return favoriteUsers;
    }

    public void setFavoriteUsers(Set<User> favoriteUsers) {
        this.favoriteUsers = favoriteUsers;
    }

    public Set<Photo> getFavoritePhotos() {
        return favoritePhotos;
    }

    public void setFavoritePhotos(Set<Photo> favoritePhotos) {
        this.favoritePhotos = favoritePhotos;
    }
}
