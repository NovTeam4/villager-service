package com.example.villagerservice.post.domain;

import com.example.villagerservice.common.domain.BaseTimeEntity;
import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.post.exception.PostException;
import lombok.Getter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

import static com.example.villagerservice.post.domain.QPost.post;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_DELETE_NOT_FOUND;

@Getter
@Entity
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

    private boolean removeTask; // 삭제요청시 false->true ( 5일뒤 자동삭제)

    protected Post() {
    }


    public Post(Member member, Category category, String title, String contents) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.contents = contents;
    }

    public void updatePost(Category category, String title, String contents) {
        this.category = category;
        this.title = title;
        this.contents = contents;
    }

    public void deletePost() {
        if (!removeTask) {
            this.removeTask = true;
        }else throw new PostException(POST_DELETE_NOT_FOUND);
    }





}
