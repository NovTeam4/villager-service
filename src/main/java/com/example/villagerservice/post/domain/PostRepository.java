package com.example.villagerservice.post.domain;
import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {

 // select * post where id and member
    Optional<Post> findByIdAndMember(Long postId,Member member);
}
