package com.example.villagerservice.post.domain;
import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<PostComment> findByIdAndPostAndMember(Long commentId, Post post, Member member);

    List<PostComment> findByPost(Post post);
}
