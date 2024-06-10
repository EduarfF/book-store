package org.example.springbootintro.dto.book;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDtoWithoutCategoryIds {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;

    public BookDtoWithoutCategoryIds(Long id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
}
