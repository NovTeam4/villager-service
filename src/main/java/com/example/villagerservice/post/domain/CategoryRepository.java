package com.example.villagerservice.post.domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.isVisible=true")
    List<Category> getCategoryList();
}

