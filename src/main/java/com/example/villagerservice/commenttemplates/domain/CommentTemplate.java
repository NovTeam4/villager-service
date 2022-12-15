package com.example.villagerservice.commenttemplates.domain;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_template_id")
    private Long id;

    private String templateContent;
    private boolean isVisible;

    CommentTemplate(String templateContent) {
        this.templateContent = templateContent;
        this.isVisible = true;
    }
}
