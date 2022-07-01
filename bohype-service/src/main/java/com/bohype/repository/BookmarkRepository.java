package com.bohype.repository;

import com.bohype.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    Optional<Bookmark> findByTitleAndLink(String title,String link);
}
