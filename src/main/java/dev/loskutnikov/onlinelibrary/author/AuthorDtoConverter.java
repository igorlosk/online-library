package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.books.BookDtoConverter;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDtoConverter {

    private final BookDtoConverter bookDtoConverter;

    public AuthorDtoConverter(BookDtoConverter bookDtoConverter) {
        this.bookDtoConverter = bookDtoConverter;
    }

    public Author toDomain(AuthorDto author) {
        return new Author(
                author.id(),
                author.name(),
                author.birthYear(),
                author.books() == null ? List.of() :
                author.books().stream()
                        .map(bookDtoConverter::toDomain)
                        .toList()
        );
    }

    public AuthorDto toDto(Author author) {
        return new AuthorDto(
                author.id(),
                author.name(),
                author.birthYear(),
                author.books()
                        .stream()
                        .map(bookDtoConverter::toDto).toList()
        );
    }
}
