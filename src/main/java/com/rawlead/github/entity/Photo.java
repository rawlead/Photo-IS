package com.rawlead.github.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "photos")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    private String title;

    @Size(max = 150)
    private String description;

    @NotNull
    @Column(name = "date_created")
    private LocalDateTime dateCreated;

    @Column
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "favoritePhotos")
    private Set<User> favoriteOfUsers = new HashSet<>();


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "photo")
    private Set<Comment> comments = new HashSet<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_category_id", nullable = false)
    private PhotoCategory photoCategory;


    public Photo() {
        this.dateCreated = LocalDateTime.now();
    }

    public boolean addComment(Comment comment) {
        return this.comments.add(comment);
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
        return this.dateCreated.format(formatter);
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
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

    public Set<Comment> getComments() {
        return this.comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<User> getFavoriteOfUsers() {
        return favoriteOfUsers;
    }

    public void setFavoriteOfUsers(Set<User> favoriteOfUsers) {
        this.favoriteOfUsers = favoriteOfUsers;
    }
}
