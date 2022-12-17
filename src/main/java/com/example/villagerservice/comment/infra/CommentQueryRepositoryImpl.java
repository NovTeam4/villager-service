package com.example.villagerservice.comment.infra;

import com.example.villagerservice.comment.domain.CommentQueryRepository;
import com.example.villagerservice.comment.dto.CommentContentsItemDto;
import com.example.villagerservice.comment.dto.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepositoryImpl implements CommentQueryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final static int DEFAULT_LIMIT = 5;

    public void create(Long memberId, CommentDto.CommentRequest request) {
        jdbcTemplate.update(save(), request.getContents(), request.getOtherId(), request.getScore(), memberId);
    }

    @Override
    public List<CommentContentsItemDto> getCommentList() {   // 모든 후기 가져오기.
        return jdbcTemplate.query(getCommentQuery(), mapRow());
    }

    public List<CommentContentsItemDto> getCommentFindName(String comment) { // 이름으로 찾기
        String sql = findByCommentNameQuery(comment);
        return jdbcTemplate.query(sql, mapRow());
    }

    @Override
    public Long getCommentTotalCount() {
        return jdbcTemplate.queryForObject("select count(*) from comment", Long.class);
    }


    @Override
    public Long getFindByNameTotalCount(String comment) {
        return jdbcTemplate.queryForObject("select count(*) from comment where content like '%" + comment + "%'", Long.class);
    }


    public List<CommentContentsItemDto> findPagingComment(Long page, int size) {
        Long realPage = size * (page - 1);
        String sql = "select * from comment order by comment_id asc limit " + realPage + "," + size + "";
        return jdbcTemplate.query(sql, mapRow());
    }


    private String save() {
        return "insert into comment(content,other_id,score,member_id) " +
                "values(?,?,?,?)";
    }

    private String getCommentQuery() {
        return "select * from comment order by comment_id desc limit " + DEFAULT_LIMIT;
    }


    private String findByCommentNameQuery(String comment) {
        return "select * from comment where content like '%" + comment + "%'";
    }

    private RowMapper<CommentContentsItemDto> mapRow() {

        return ((rs, rowNum) -> new CommentContentsItemDto(
                rs.getLong("comment_id"),
                rs.getLong("member_id"),
                rs.getLong("score"),
                rs.getString("content"),
                rs.getLong("other_id")
        ));
    }


}
