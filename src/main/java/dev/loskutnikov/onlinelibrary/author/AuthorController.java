package dev.loskutnikov.onlinelibrary.author;

import jakarta.validation.Valid;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorDtoConverter dtoConverter;
    Logger log = LoggerFactory.getLogger(AuthorController.class);

    public AuthorController(AuthorService authorService, AuthorDtoConverter dtoConverter) {
        this.authorService = authorService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
            @RequestBody @Valid AuthorDto authorToCreate
    ) {
        log.info("Get request to create author: author={}", authorToCreate);
        Author createdAuthor = authorService.createAuthor(dtoConverter.toDomain(authorToCreate));
        return ResponseEntity.status(HttpStatus.CREATED).body(dtoConverter.toDto(createdAuthor));
    }

    @GetMapping
    public List<AuthorDto> getAllAuthors() {
        log.info("Get request to get all authors");
        return authorService.getAllAuthors().stream()
                .map(dtoConverter::toDto)
                .toList();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("id") Long authorId) {
        log.info("Get request to delete author: id={}", authorId);
        authorService.deleteAuthor(authorId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
