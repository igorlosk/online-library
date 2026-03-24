package dev.loskutnikov.onlinelibrary.books;

import dev.loskutnikov.onlinelibrary.author.AuthorService;
import jakarta.persistence.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookEntityConverter bookEntityConverter;

    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, BookEntityConverter bookEntityConverter, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.bookEntityConverter = bookEntityConverter;
        this.authorService = authorService;
    }

    public Book createBook(Book bookToCreate) {

        checkAuthorExistance(bookToCreate.authorId());

        var bookToSave = bookEntityConverter.toEntity(bookToCreate);
        var savedEntity = bookRepository.save(bookToSave);

        return bookEntityConverter.toDomain(savedEntity);
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
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, Book bookToUpdate) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        checkAuthorExistance(bookToUpdate.id());

        bookRepository.updateBook(
                id,
                bookToUpdate.name(),
                bookToUpdate.authorId(),
                bookToUpdate.publicationYear(),
                bookToUpdate.pageNumber(),
                bookToUpdate.cost()
        );

        return bookEntityConverter.toDomain(bookRepository.findById(id).orElseThrow());
    }

    private void checkAuthorExistance(Long authorId) {
        if (!authorService.isAuthorExistsById(authorId)) {
            throw new IllegalArgumentException("Author with id " + authorId + " not found");
        }
    }
}
