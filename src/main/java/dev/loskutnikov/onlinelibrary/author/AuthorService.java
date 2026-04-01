package dev.loskutnikov.onlinelibrary.author;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorEntityConverter entityConverter;

    public AuthorService(AuthorRepository authorRepository, AuthorEntityConverter entityConverter) {
        this.authorRepository = authorRepository;
        this.entityConverter = entityConverter;
    }

    public Author createAuthor(Author author) {
        if (authorRepository.existsByName(author.name())) {
            throw new IllegalArgumentException("Author name already exists!");
        }
        var entityToSave = entityConverter.toEntity(author);
        return entityConverter.toDomain(authorRepository.save(entityToSave));
    }

    public boolean isAuthorExistsById(Long id) {
        return authorRepository.existsById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAllWithBooks().stream()
                .map(entityConverter::toDomain)
                .toList();
    }

    @Transactional
    public void deleteAuthor(Long authorId) {
        if (!authorRepository.existsById(authorId)) {
            throw new EntityNotFoundException("Author does not exists by id=%s".formatted(authorId));
        }
        authorRepository.deleteAuthorFromBooks(authorId);
//        if (1 == 1) {
//            throw new RuntimeException("Test exception");
//        }
        authorRepository.deleteById(authorId);
    }
}
