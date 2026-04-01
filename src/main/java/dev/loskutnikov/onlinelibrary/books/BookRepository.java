package dev.loskutnikov.onlinelibrary.books;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
//    List<BookEntity> findAllByAuthorNameIsAndCostLessThan(
//            String authorName,
//            Integer maxCost
//    );

    @Query("""
            SELECT b FROM BookEntity b
            where (:authorId IS NULL OR b.authorId = :authorId)
            AND (:cost IS NULL OR b.cost < :cost)
            """)
    List<BookEntity> searchBooks(
            Long authorId,
            @Param("cost") Integer maxCost,
            Pageable pageable);

    @Query(value = """
            SELECT * FROM books b
            where (:authorId IS NULL OR b.authorId = :authorId)
            AND (:cost IS NULL OR b.cost < :cost)
            LIMIT 10
            OFFSET 50
            """, nativeQuery = true)
    List<BookEntity> searchBooksNative(
            Long authorId,
            @Param("cost") Integer maxCost);

    @Transactional
    @Modifying
    @Query("""
            UPDATE BookEntity b SET 
            b.name = :name,
            b.authorId = :authorId,
            b.publicationYear = :pubYear,
            b.pageNumber = :pageNum,
            b.cost = :cost
                        where b.id = :id
            
            """)
    void updateBook(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("authorId") Long authorId,
            @Param("pubYear") Integer pubYear,
            @Param("pageNum") Integer pageNum,
            @Param("cost") Integer cost

    );
}
