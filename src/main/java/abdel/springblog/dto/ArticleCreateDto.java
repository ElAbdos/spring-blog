package abdel.springblog.dto;

import lombok.Getter;

@Getter
public class ArticleCreateDto {
    private String authorUsername;
    private String content;

    public ArticleCreateDto() {}
}
