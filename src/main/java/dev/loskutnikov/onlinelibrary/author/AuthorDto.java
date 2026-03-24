package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.books.BookDto;
import jakarta.validation.constraints.*;

import java.util.List;

//сущность слоя контроллеров

public record AuthorDto(
        @Null
        Long id,
        @NotBlank
        String name,
        @Min(0)
        Integer birthYear,
        @Size(max = 0)
        List<BookDto> books
) {
}
