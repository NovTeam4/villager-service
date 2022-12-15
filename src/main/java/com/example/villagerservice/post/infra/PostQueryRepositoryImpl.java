package com.example.villagerservice.post.infra;

import com.example.villagerservice.post.domain.PostQueryRepository;
import com.example.villagerservice.post.dto.*;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.villagerservice.member.domain.QMember.member;
import static com.example.villagerservice.member.domain.QMemberDetail.memberDetail;
import static com.example.villagerservice.post.domain.QCategory.category;
import static com.example.villagerservice.post.domain.QPost.post;
import static com.example.villagerservice.post.domain.QPostComment.postComment;
import static com.example.villagerservice.post.domain.QPostImage.postImage;
import static com.example.villagerservice.postlike.domain.QPostLike.postLike;
import static com.querydsl.core.types.ExpressionUtils.count;
import static com.querydsl.core.types.dsl.Wildcard.count;

@Repository
@RequiredArgsConstructor
public class PostQueryRepositoryImpl implements PostQueryRepository {

    @Value("${aws.s3.base-url}")
    private String awsS3Baseurl;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ListPostItem> getPostList(ListPostSearchCond cond, Pageable pageable) {
        List<ListPostItem> contents = queryFactory
                .select(
                        Projections.constructor(ListPostItem.class,
                                post.id,
                                category.id,
                                memberDetail.nickname,
                                post.viewCount,
                                post.title,
                                category.name,
                                post.createdAt,
                                ExpressionUtils.as(
                                        JPAExpressions.select(count(postLike.id))
                                                .from(postLike)
                                                .where(postLike.post.eq(post)),
                                        "postLikeCount")))
                .from(post)
                .innerJoin(post.member, member)
                .innerJoin(post.member.memberDetail, memberDetail)
                .innerJoin(post.category, category)
                .where(
                        isActivePost(),
                        isSameCategory(cond.getCategoryId()),
                        isSamePostTitle(cond.getTitle()))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.createdAt.desc())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(count)
                .from(post)
                .where(isActivePost());

        return PageableExecutionUtils.getPage(
                contents,
                pageable,
                countQuery::fetchOne);
    }

    @Override
    public PostItemDetail getPostItemDetail(Long postId) {

        PostItemDetail postItemDetail = queryFactory
                .select(Projections.constructor(PostItemDetail.class,
                        post.id,
                        category.id,
                        memberDetail.nickname,
                        post.viewCount,
                        post.title,
                        post.contents,
                        category.name,
                        post.createdAt,
                        ExpressionUtils.as(
                                JPAExpressions.select(count(postLike.id))
                                        .from(postLike)
                                        .where(postLike.post.eq(post)),
                                "postLikeCount")))
                .from(post)
                .innerJoin(post.member, member)
                .innerJoin(member.memberDetail, memberDetail)
                .innerJoin(post.category, category)
                .where(isActivePost(),
                        post.id.eq(postId)).
                fetchOne();

        List<PostImageItem> images = queryFactory
                .select(Projections.constructor(PostImageItem.class,
                        postImage.id,
                        postImage.path.prepend(awsS3Baseurl),
                        postImage.size
                ))
                .from(postImage)
                .innerJoin(postImage.post, post)
                .where(isActivePost(),
                        isSamePost(postId))
                .fetch();


        List<PostCommentItem> postComments = queryFactory
                .select(Projections.constructor(PostCommentItem.class,
                        postComment.id,
                        member.id,
                        memberDetail.nickname,
                        postComment.contents,
                        postComment.createdAt))
                .from(postComment)
                .innerJoin(postComment.post, post)
                .innerJoin(postComment.member, member)
                .innerJoin(member.memberDetail, memberDetail)
                .where(isActivePost(),
                        isSamePost(postId))
                .orderBy(postComment.createdAt.desc())
                .fetch();

        if (postItemDetail != null) {
            postItemDetail.setImages(images);
        }

        if (postItemDetail != null) {
            postItemDetail.setComments(postComments);
        }

        return postItemDetail;
    }

    private static BooleanExpression isSamePost(Long postId) {
        return post.id.eq(postId);
    }

    private BooleanExpression isActivePost() {
        return post.isDeleted.eq(false);
    }

    private BooleanExpression isSameCategory(Long categoryId) {
        if (categoryId == null || categoryId == 0L) {
            return null;
        }
        return post.category.id.eq(categoryId);
    }

    private BooleanExpression isSamePostTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        return post.title.startsWith(title);
    }
}
