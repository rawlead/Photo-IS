package com.rawlead.github.repository;

import com.rawlead.github.entity.PhotoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoCategoryRepository extends JpaRepository<PhotoCategory, Long> {

    PhotoCategory findByName(String name);



}
