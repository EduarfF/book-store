package org.example.springbootintro.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookRequestDto {
    @NotBlank(message = "title can not be blank")
    @Size(max = 255, message = "max title length is 255 characters")
    private String title;

    @NotBlank(message = "author can not be blank")
    @Size(max = 255, message = "max author length is 255 characters")
    private String author;

    @NotBlank(message = "isbn can not be blank")
    @Size(min = 10, message = "min isbn length is 10 characters")
    @Size(max = 255, message = "max isbn length is 255 characters")
    private String isbn;

    @NotNull(message = "price can not be null")
    @Min(value = 0, message = "min price value is 0")
    private BigDecimal price;

    @Size(min = 25, message = "min description length is 25 characters")
    @Size(max = 255, message = "max description length is 255 characters")
    private String description;

    private String coverImage;

    @NotEmpty(message = "CategoryIds can not be empty")
    @Size(min = 1, message = "Book must have at least 1 category")
    private Set<Long> categoryIds;
}
