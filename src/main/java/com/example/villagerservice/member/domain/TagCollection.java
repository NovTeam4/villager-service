package com.example.villagerservice.member.domain;

import com.example.villagerservice.common.utils.DeduplicationUtils;
import com.example.villagerservice.member.exception.MemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.villagerservice.member.exception.MemberErrorCode.MEMBER_TAG_MAX_COUNT;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagCollection {
    public static final int MAX_TAG_COUNT = 5;
    @ElementCollection
    @CollectionTable(
            name = "member_tag",
            joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "name")
    private List<Tag> tags = new ArrayList<>();
    public void addTags(List<Tag> insertTags) {
        // 중복 제거
        insertTags = DeduplicationUtils.deduplication(insertTags, Tag::getName);

        if (insertTags.size() > MAX_TAG_COUNT) {
            throw new MemberException(MEMBER_TAG_MAX_COUNT);
        }
        this.tags = insertTags;
    }
    public int getTagCount() {
        return tags.size();
    }
}
