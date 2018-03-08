package com.rawlead.github.repository;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo,Long> {
    List<Photo> findByUserId(Long id);

}
