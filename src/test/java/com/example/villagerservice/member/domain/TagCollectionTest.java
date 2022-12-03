package com.example.villagerservice.member.domain;

import com.example.villagerservice.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.example.villagerservice.member.exception.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TagCollectionTest {


    @Test
    @DisplayName("관심태그 추가 시 최대5개 초과될 경우 테스트")
    void addTagsMaxOverTest() {
        // given
        TagCollection tagCollection = new TagCollection();
        List<Tag> insertTags = new ArrayList<>();
        for (int i = 0; i < TagCollection.MAX_TAG_COUNT + 1; i++) {
            insertTags.add(new Tag(String.format("%s번 태그 insert", i + 1)));
        }

        // when
        MemberException memberException
                = assertThrows(MemberException.class, () -> tagCollection.addTags(insertTags));

        // then
        assertThat(memberException.getMemberErrorCode()).isEqualTo(MEMBER_TAG_MAX_COUNT);
        assertThat(memberException.getErrorCode()).isEqualTo(MEMBER_TAG_MAX_COUNT.getErrorCode());
        assertThat(memberException.getErrorMessage()).isEqualTo(MEMBER_TAG_MAX_COUNT.getErrorMessage());
    }

    @Test
    @DisplayName("관심태그 추가 테스트")
    void addTagsTest() {
        // given
        TagCollection tagCollection = new TagCollection();
        List<Tag> insertTags = new ArrayList<>();
        for (int i = 0; i < TagCollection.MAX_TAG_COUNT; i++) {
            insertTags.add(new Tag(String.format("%s번 태그 insert", i + 1)));
        }

        // when
        tagCollection.addTags(insertTags);

        // then
        assertThat(tagCollection.getTagCount()).isEqualTo(TagCollection.MAX_TAG_COUNT);
    }
}