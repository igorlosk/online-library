package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.books.BookEntityConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class AuthorEntityConverter {

    public final BookEntityConverter bookEntityConverter;

    public AuthorEntityConverter(BookEntityConverter bookEntityConverter) {
        this.bookEntityConverter = bookEntityConverter;
    }

    public AuthorEntity toEntity(Author author) {
            return new AuthorEntity(
                    author.id(),
                    author.name(),
                    author.birthYear(),
                    author.books().stream()
                            .map(bookEntityConverter::toEntity)
                            .collect(Collectors.toSet()));

    }

    public Author toDomain(AuthorEntity authorEntity) {
        return new Author(
                authorEntity.getId(),
                authorEntity.getName(),
                authorEntity.getBornYear(),
                authorEntity.getBooks().stream()
                        .map(bookEntityConverter::toDomain)
                        .toList()
        );
    }
}
