package com.example.villagerservice.post.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;

import com.example.villagerservice.postlike.domain.PostLike;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.example.villagerservice.post.exception.PostException;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.villagerservice.post.exception.PostErrorCode.POST_DELETE_NOT_FOUND;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;        // Post Id값

    @Column(length = 16)
    private String title;    // 제목
    @Lob
    private String contents; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;   // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리
    private long viewCount;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PostImage> images = new ArrayList<>();
    private boolean isDeleted; // 삭제요청시 false->true ( 5일뒤 자동삭제)

    public Post(Member member, Category category, String title, String contents) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.viewCount = 0;
    }

    public void updatePost(Category category, String title, String contents) {
        this.category = category;
        this.title = title;
        this.contents = contents;
    }


    public void addImages(PostImage postImage) {
        this.images.add(postImage);
        postImage.postReference(this);
    }

    public void deletePost() {
        if (!isDeleted) {
            this.isDeleted = true;
        }else throw new PostException(POST_DELETE_NOT_FOUND);
    }
}
