package dev.loskutnikov.onlinelibrary.books;

import dev.loskutnikov.onlinelibrary.author.AuthorService;
import dev.loskutnikov.onlinelibrary.books.event.BookEventSender;
import dev.loskutnikov.onlinelibrary.books.event.BookKafkaEvent;
import dev.loskutnikov.onlinelibrary.books.event.EventType;
import jakarta.persistence.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookEntityConverter bookEntityConverter;

    private final AuthorService authorService;

    private final BookEventSender bookEventSender;

    public BookService(BookRepository bookRepository, BookEntityConverter bookEntityConverter, AuthorService authorService, BookEventSender bookEventSender) {
        this.bookRepository = bookRepository;
        this.bookEntityConverter = bookEntityConverter;
        this.authorService = authorService;
        this.bookEventSender = bookEventSender;
    }

    public Book createBook(Book bookToCreate) {

        checkAuthorExistance(bookToCreate.authorId());

        var bookToSave = bookEntityConverter.toEntity(bookToCreate);
        var savedBook = bookEntityConverter.toDomain(bookRepository.save(bookToSave));

        bookEventSender.sendEvent(new BookKafkaEvent(
                savedBook.id(),
                EventType.CREATED,
                savedBook
        ));

        return savedBook;
    }


    public List<Book> searchAllBooks(BookSearchFilter bookSearchFilter) {

        int pageSize = bookSearchFilter.pageSize() != null ? bookSearchFilter.pageSize() : 3;
        int pageNumber = bookSearchFilter.pageNumber() != null ? bookSearchFilter.pageNumber() : 0;


        Pageable pageable = Pageable
                .ofSize(pageSize)
                .withPage(pageNumber);

        return bookRepository.searchBooks(
                        bookSearchFilter.authorId(),
                        bookSearchFilter.maxCost(),
                        pageable
                ).stream().map(bookEntityConverter::toDomain)
                .toList();

    }

    public Book findById(Long id) {

        BookEntity bookEntity = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        return bookEntityConverter.toDomain(bookEntity);
    }

    public void deleteBookById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        bookEventSender.sendEvent(new BookKafkaEvent(
                id,
                EventType.REMOVED,
                null
        ));
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book bookToUpdate) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        checkAuthorExistance(bookToUpdate.authorId());

        bookRepository.updateBook(
                id,
                bookToUpdate.name(),
                bookToUpdate.authorId(),
                bookToUpdate.publicationYear(),
                bookToUpdate.pageNumber(),
                bookToUpdate.cost()
        );
        var updatedBook = bookEntityConverter.toDomain(bookRepository.findById(id).orElseThrow());

        bookEventSender.sendEvent(new BookKafkaEvent(
                id,
                EventType.UPDATED,
                updatedBook
        ));

        return updatedBook;
    }

    private void checkAuthorExistance(Long authorId) {
        if (!authorService.isAuthorExistsById(authorId)) {
            throw new IllegalArgumentException("Author with id " + authorId + " not found");
        }
    }
}
