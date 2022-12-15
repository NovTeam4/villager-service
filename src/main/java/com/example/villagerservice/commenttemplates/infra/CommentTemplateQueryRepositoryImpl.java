package com.example.villagerservice.commenttemplates.infra;

import com.example.villagerservice.commenttemplates.domain.CommentTemplateQueryRepository;
import com.example.villagerservice.commenttemplates.dto.CommentTemplateItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentTemplateQueryRepositoryImpl implements CommentTemplateQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<CommentTemplateItemDto> getCommentTemplate() {
        return jdbcTemplate.query(getPartyQuery(), mapRow());
    }

    @Override
    public Long getCommentTemplateTotalCount() {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM COMMENT_TEMPLATE WHERE is_visible = TRUE", Long.class);
    }

    private String getPartyQuery() {
        return "SELECT * FROM comment_template WHERE is_visible = TRUE";
    }

    private RowMapper<CommentTemplateItemDto> mapRow() {

        return ((rs, rowNum) -> new CommentTemplateItemDto(
                rs.getLong("comment_template_id"),
                rs.getString("TEMPLATE_CONTENT")
        ));
    }
}
