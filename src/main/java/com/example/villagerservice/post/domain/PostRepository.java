package com.example.villagerservice.post.domain;

import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndMember(Long postId, Member member);

    @Query("select p from Post p join fetch p.category where p.id = :postId")
    Optional<Post> getPostWithCategory(@Param("postId") Long postId);
}
