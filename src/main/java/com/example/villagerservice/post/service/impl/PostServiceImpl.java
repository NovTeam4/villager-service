package com.example.villagerservice.post.service.impl;

import com.example.villagerservice.member.domain.Member;
import com.example.villagerservice.member.domain.MemberRepository;
import com.example.villagerservice.member.exception.MemberException;
import com.example.villagerservice.post.domain.*;
import com.example.villagerservice.post.dto.CreatePost;
import com.example.villagerservice.post.dto.ListPost;
import com.example.villagerservice.post.dto.UpdatePost;
import com.example.villagerservice.post.exception.CategoryException;
import com.example.villagerservice.post.exception.PostException;
import com.example.villagerservice.post.service.FileUploadService;
import com.example.villagerservice.post.service.PostService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static com.example.villagerservice.post.domain.QPost.post;
import static com.example.villagerservice.post.exception.CategoryErrorCode.CATEGORY_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_NOT_FOUND;
import static com.example.villagerservice.post.exception.PostErrorCode.POST_VALID_NOT;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final FileUploadService fileUploadService;
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public void createPost(Long memberId, CreatePost.Request request, List<MultipartFile> images) {
        Member member = findByMemberId(memberId);
        Category category = findByCategoryId(request.getCategoryId());

        Post post = new Post(member, category, request.getTitle(), request.getContents());

        List<String> imagePaths = new ArrayList<>();
        for (MultipartFile image : images) {
            String imagePath = UUID.randomUUID() + "-" + image.getOriginalFilename();
            post.addImages(PostImage.createPostImage(image.getSize(), imagePath));
            imagePaths.add(imagePath);
        }
        postRepository.save(post);
        fileUploadService.fileUpload(images, imagePaths);
    }

    @Transactional
    @Override
    public void updatePost(Long memberId, Long postId, UpdatePost.Request request) {
        Member member = findByMemberId(memberId);
        Post post = findByPostIdAndMember(postId, member);  // 맴버아이디+게시글id 일치하는지
        Category category = findByCategoryId(request.getCategoryId());
        post.updatePost(category, request.getTitle(), request.getContents());

    }

    @Transactional
    @Override
    public void deletePost(Long memberId, Long postId) {
        Member member = findByMemberId(memberId);
        Post post = findByPostIdAndMember(postId, member);
        post.deletePost();
    }

    @Override
    public List<ListPost.Response> getList() {

        List<Post> posts = postRepository.findAll();

             return posts.stream()
                     .map(p -> new ListPost.Response(
                                     p.getTitle()
                                     , p.getMember().getMemberDetail().getNickname()
                             )
                     ).collect(Collectors.toList());

    }


    private Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    private Category findByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException(CATEGORY_NOT_FOUND));
    }

    private Post findByPostIdAndMember(Long postId, Member member) {
        return postRepository.findByIdAndMember(postId, member).orElseThrow(() -> new PostException(POST_VALID_NOT));
    }


    private List<Post> findByPostList() {
        try {
            int postTotalPageCount = getPostTotalPageCount();
            int start = 0;
            int end = 10;

            for (int i = 0; i < postTotalPageCount; i++) {

            }

        } catch (PostException e) {
            throw new PostException(POST_NOT_FOUND);
        }
        return null;
    }
    private int getPostTotalPageCount() throws PostException {

        List<Post> posts = postRepository.findAll();

        int totalCount = posts.size();  // 게시판 총 갯수

        int count = (totalCount / 10); // 몫

        if (totalCount % 10 > 0) {  // 나머지가 1개라도 존재한다면
            count++;
        }

        return count;
    }


    @Transactional
    @Scheduled(cron = "59 59 23 * * *")   //  매일 23시59분59초마다 스케쥴링체크
    public void removeTaskSchedule() {

        jpaQueryFactory.delete(post)
                .where(post.removeTask.isTrue())
                .where(timeMinus())
                .execute();
    }

    private BooleanExpression timeMinus() {
        var date = LocalDateTime.now().minusDays(6);
//        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh-mm");
        return post.modifiedAt.loe(date); //    <gt

        // date  = 현재시간을 계속받아올건데 - 2분을뺴고
        //  최종작성삭제시간이랑 비교를할건데   date
        // 12: 38            <=     date(  12:41-2 = 12:39)  => 12:38:12:38

        // loe가 작거나 같을떄?
        //
    }


}
