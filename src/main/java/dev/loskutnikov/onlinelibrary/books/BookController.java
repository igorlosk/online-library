package dev.loskutnikov.onlinelibrary.books;

import dev.loskutnikov.onlinelibrary.books.purchase.PurchaseService;
import dev.loskutnikov.onlinelibrary.security.jwt.AuthenticationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final static Logger log = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    private final BookDtoConverter bookDtoConverter;

    private final AuthenticationService authenticationService;

    private final PurchaseService purchaseService;

    public BookController(BookService bookService, BookDtoConverter bookDtoConverter, AuthenticationService authenticationService, PurchaseService purchaseService) {
        this.bookService = bookService;
        this.bookDtoConverter = bookDtoConverter;
        this.authenticationService = authenticationService;
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public List<BookDto> getAllBooks(
            @Valid BookSearchFilter bookSearchFilter) {
        log.info("Getting all books");
        return bookService.searchAllBooks(bookSearchFilter)
                .stream()
                .map(bookDtoConverter::toDto)
                .toList();
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookDto bookDtoToCreate) {
        log.info("Creating new book {}", bookDtoToCreate);
        var createdBook = bookService.createBook(bookDtoConverter.toDomain(bookDtoToCreate));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookDtoConverter.toDto(createdBook));
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable Long id) {
        log.info("Getting book with id {}", id);
        var foundBook = bookService.findById(id);
        return bookDtoConverter.toDto(foundBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        log.info("Deleting book with id {}", id);
        bookService.deleteBookById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid BookDto bookDtoToUpdate) {
        log.info("Updating book {}", bookDtoToUpdate);
        Book updatedBook = bookService
                .updateBook(id, bookDtoConverter.toDomain(bookDtoToUpdate));
        return ResponseEntity.ok(bookDtoConverter.toDto(updatedBook));
    }

    @PostMapping("/{id}/purchase")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> purchaseBook(@PathVariable("id") Long bookId){
        log.info("Get book from purchase: bookId={}", bookId);
        var currentUser = authenticationService.getCurrentAuthenticatedUserOrThrow();
        purchaseService.performBookPurchase(currentUser, bookId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
