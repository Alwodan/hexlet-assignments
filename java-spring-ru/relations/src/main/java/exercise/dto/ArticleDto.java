package exercise.dto;

import exercise.model.Category;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Getter
@Setter
public class ArticleDto {
    private String name;
    private String body;
    private Category category;
}
// END
