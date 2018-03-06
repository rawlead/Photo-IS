package com.rawlead.github.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dateCreated;

    private String url;

    @JsonIgnore
    @OneToMany(mappedBy = "photo",fetch = FetchType.EAGER)
    private List<Comment> comments;

    @ManyToOne
    @JoinColumn
    private PhotoCategory photoCategory;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> userFavorites;


    public boolean addUserFavorite(User user) {
        if (this.userFavorites == null)
            this.userFavorites = new HashSet<>();
        return this.userFavorites.add(user);
    }

    public boolean deleteUserFavorite(User user) {
        return this.userFavorites != null && !this.userFavorites.isEmpty() && this.userFavorites.remove(user);
    }

    public Photo() {
        this.dateCreated = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return  this.dateCreated.format(formatter);
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<User> getUserFavorites() {
        return userFavorites;
    }

    public void setUserFavorites(Set<User> userFavorites) {
        this.userFavorites = userFavorites;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PhotoCategory getPhotoCategory() {
        return photoCategory;
    }

    public void setPhotoCategory(PhotoCategory photoCategory) {
        this.photoCategory = photoCategory;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
