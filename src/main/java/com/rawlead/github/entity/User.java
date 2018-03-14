package com.rawlead.github.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @NotNull
    private String email;

    @NotNull
    private String username;

    @JsonIgnore
    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role> roles;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Photo> photos = new HashSet<>();


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "favorite_photos",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "photo_id")})
    private Set<Photo> favoritePhotos = new HashSet<>();


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_users",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "favorite_user_id")})
    private Set<User> favoriteUsers = new HashSet<>();


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_users",
            joinColumns = {@JoinColumn(name = "favorite_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<User> favoriteUserOf = new HashSet<>();


    public User() {
        this.registrationDate = LocalDateTime.now();
    }

    public User(String firstName, String lastName, String email, String username, String password, Set<Role> roles, String avatarUrl) {
        this.registrationDate = LocalDateTime.now();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.avatarUrl = avatarUrl;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public boolean addFavoriteUser(User favoriteUser) {
        return this.favoriteUsers.add(favoriteUser);
    }

    public boolean deleteFavoriteUser(User favoriteUser) {
        return this.favoriteUsers != null && !this.favoriteUsers.isEmpty() && this.favoriteUsers.remove(favoriteUser);
    }

    public boolean hasFavoriteUser(User favoriteUser) {
        return this.favoriteUsers.contains(favoriteUser);
    }

    public boolean addFavoritePhoto(Photo favoritePhoto) {
        return this.favoritePhotos.add(favoritePhoto);
    }

    public boolean deleteFavoritePhoto(Photo favoritePhoto) {
        return this.favoritePhotos != null && !this.favoritePhotos.isEmpty() && this.favoritePhotos.remove(favoritePhoto);
    }

    public boolean hasFavoritePhoto(Photo photo) {
        return this.photos.contains(photo);
    }

    public String getRegistrationDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.registrationDate.format(formatter);
    }

    public boolean addComment(Comment comment) {
        return this.comments.add(comment);
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
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

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getFavoriteUserOf() {
        return favoriteUserOf;
    }

    public void setFavoriteUserOf(Set<User> favoriteUserOf) {
        this.favoriteUserOf = favoriteUserOf;
    }
}
