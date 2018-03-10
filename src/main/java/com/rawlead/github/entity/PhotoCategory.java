package com.rawlead.github.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "photo_categories")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class PhotoCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "photoCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Photo> photos = new ArrayList<>();

    public PhotoCategory() {
    }

    public PhotoCategory(String name, List<Photo> photos) {
        this.name = name;
        this.photos = photos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }
}
