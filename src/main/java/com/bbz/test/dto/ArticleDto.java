package com.bbz.test.dto;

import com.bbz.test.validation.validator.ValidTitle;

import javax.validation.constraints.NotBlank;

public class ArticleDto {

    @NotBlank(message = "{title.notSpace})")
    @ValidTitle(message = "{title.tooLong}")
    private String title;

    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ArticleDto{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
