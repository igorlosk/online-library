package dev.loskutnikov.onlinelibrary.author;

import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("""
            UPDATE BookEntity b
            SET b.authorId = NULL WHERE b.authorId = :authorId
            """)
    void deleteAuthorFromBooks(Long authorId);

    // N+1 решение
//    @Query("""
//            SELECT a from AuthorEntity a
//                        JOIN FETCH a.books
//            """)
//    List<AuthorEntity> findAllWithBooks ();

    // N+1 решение
//    @Query("SELECT a FROM AuthorEntity a")
//    @EntityGraph(attributePaths = "books")
//    @EntityGraph(attributePaths = {"books", "years"})
//    List<AuthorEntity> findAllWithBooks();

    @Query("SELECT a FROM AuthorEntity a")
    @EntityGraph(value = "author-with-books")
    List<AuthorEntity> findAllWithBooks();
}
