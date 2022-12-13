package com.example.villagerservice.post.domain;
import com.example.villagerservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    Optional<PostComment> findByIdAndPostAndMember(Long commentId, Post post, Member member);
}
