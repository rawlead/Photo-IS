package com.rawlead.github.repository;

import com.rawlead.github.entity.PhotoObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PhotoObject,Long> {
    List<PhotoObject> findByCreatorId(Long id);
}
