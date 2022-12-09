package com.example.villagerservice.post.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.post.domain.Category;
import com.example.villagerservice.post.domain.CategoryRepository;
import com.example.villagerservice.post.domain.Post;
import com.example.villagerservice.post.domain.PostRepository;
import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;


    @Transactional
    @Override
    public void createPost(Long memberId, CreatePost.Request request) {
        postRepository.save(dtoToEntity(memberId,request));
    }



    public Post dtoToEntity(Long memberId, CreatePost.Request request){

        Category category = categoryRepository.findById(request.getCategoryid()).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();
        Post post = new Post(member,category, request.getTitle(), request.getContetns());

        return post;
    }
}
