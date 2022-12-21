package com.example.villagerservice.post.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private Long size;
    private String path;

    private PostImage(Long size, String path) {
        this.size = size;
        this.path = path;
    }

    public static PostImage createPostImage(Long size, String path) {
        return new PostImage(size, path);
    }

    public void postReference(Post post) {
        this.post = post;
    }
}
