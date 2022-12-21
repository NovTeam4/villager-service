package com.example.villagerservice.postlike.domain;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.domain.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private PostLike(Member member, Post post) {
        this.member = member;
        this.post = post;
    }

    public static PostLike createPost(Member member, Post post) {
        return new PostLike(member, post);
    }
}
