package com.example.villagerservice.post.domain;

import com.example.villagerservice.member.domain.Member;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;        // Post Id값

    @Column(length = 16)
    private String title;    // 제목
    @Lob
    private String contetns; // 내용

    @ManyToOne
    private Member member;   // 회원

    protected Post() {
    }


    public Post(Member member, String title, String contetns) {
        this.member = member;
        this.title = title;
        this.contetns = contetns;
    }

}
