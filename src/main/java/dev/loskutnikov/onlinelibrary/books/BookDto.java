package dev.loskutnikov.onlinelibrary.books;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookDto(
        @Null
        Long id,

        @NotBlank(message = "Не должно быть пустым")
        @Size(max = 30)
        String name,

//        @NotBlank
//        @Size(max = 30)
        Long authorId,

        @JsonProperty("pubYear")
        @Min(0)
        @NotNull
        Integer publicationYear,

        @JsonProperty("pageNum")
        @Min(1)
        @Max(10000)
        @NotNull
        Integer pageNumber,

        @NotNull
        @Max(100000)
        @Min(0)
        Integer cost
) {
}
