package com.example.villagerservice.comment.service;

import com.example.villagerservice.comment.domain.CommentQueryRepository;
import com.example.villagerservice.comment.dto.CommentContentsItemDto;
import com.example.villagerservice.comment.dto.CommentDto;
import com.example.villagerservice.comment.excepiton.CommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.villagerservice.comment.excepiton.CommentErrorCode.COMMENT_PAGE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    private final int NUM_DIVDEN_IN_PAGE = 5;

    private final CommentQueryRepository commentQueryRepository;

    @Override
    public void commentCreate(Long memberId, CommentDto.CommentRequest request) {
        commentQueryRepository.create(memberId, request);
    }

    @Override   // 후기  전체조회
    public CommentDto.findByAllResponse getCommentList() {
        Long commentTotalCount = commentQueryRepository.getCommentTotalCount();
        List<CommentContentsItemDto> commentList = commentQueryRepository.getCommentList();
        return new CommentDto.findByAllResponse(commentTotalCount, commentList);
    }

    @Override // 내용으로 후기 조회
    public CommentDto.findByNameResponse getFindName(String name) {
        Long findByNameTotalCount = commentQueryRepository.getFindByNameTotalCount(name);
        List<CommentContentsItemDto> commentFindName = commentQueryRepository.getCommentFindName(name);
        return new CommentDto.findByNameResponse(findByNameTotalCount, commentFindName);
    }


    // ***********************************        페이징 쿼리     ******************************************************

    public Long getTotalPageCount() {
        Long totalCount = commentQueryRepository.getCommentTotalCount();
        Long pageDiv = (totalCount / NUM_DIVDEN_IN_PAGE);

        if ((totalCount % NUM_DIVDEN_IN_PAGE) > 0) {
            pageDiv++;
        }
        return pageDiv;
    }

    // Ex) findPagingComment(3) => 3페이지 dto 리스트 출력
    public CommentDto.findPagingResponse findPagingComment(Long page) {
        Long totalPageCount = getTotalPageCount();
        Long totalCount = commentQueryRepository.getCommentTotalCount();

        if (page > totalPageCount || page <= 0) {
            throw new CommentException(COMMENT_PAGE_NOT_FOUND);
        }

        List<CommentContentsItemDto> pagingComment = commentQueryRepository.findPagingComment(page, NUM_DIVDEN_IN_PAGE);


        return new CommentDto.findPagingResponse(totalCount, totalPageCount, NUM_DIVDEN_IN_PAGE, page, pagingComment);
    }
}
