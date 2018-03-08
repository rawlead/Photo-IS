package com.rawlead.github.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    @OneToMany(mappedBy = "photo", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @ManyToOne
    @JoinColumn
    private PhotoCategory photoCategory;

    @ManyToOne
    @JoinColumn
    private User user;

    public Photo() {
        this.dateCreated = LocalDateTime.now();
    }

    public boolean addComment(Comment comment) {
        if (this.comments == null)
            this.comments = new TreeSet<>();
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
}
