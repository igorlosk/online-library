package dev.loskutnikov.onlinelibrary.author;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("""
            UPDATE BookEntity b
            SET b.authorId = NULL WHERE b.authorId = :authorId
            """)
    void deleteAuthorFromBooks(Long authorId);

}
