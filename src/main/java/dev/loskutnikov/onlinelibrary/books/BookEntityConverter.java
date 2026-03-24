package dev.loskutnikov.onlinelibrary.books;

import org.springframework.stereotype.Component;

@Component
public class BookEntityConverter {

    public BookEntity toEntity(Book book) {
        return new BookEntity(
                book.id(),
                book.name(),
                book.authorId(),
                book.publicationYear(),
                book.pageNumber(),
                book.cost()
        );
    }

    public Book toDomain(BookEntity bookEntity) {
        return new Book(
                bookEntity.getId(),
                bookEntity.getName(),
                bookEntity.getAuthorId(),
                bookEntity.getPublicationYear(),
                bookEntity.getPageNumber(),
                bookEntity.getCost()
        );
    }
}
