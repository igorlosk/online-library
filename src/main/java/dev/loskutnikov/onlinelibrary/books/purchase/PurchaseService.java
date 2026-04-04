package dev.loskutnikov.onlinelibrary.books.purchase;

import dev.loskutnikov.onlinelibrary.books.BookService;
import dev.loskutnikov.onlinelibrary.users.User;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
@Transactional
public class PurchaseService {

    private final BookService bookService;

    private final PurchaseRepository purchaseRepository;

    private final static Logger log = LoggerFactory.getLogger(PurchaseService.class);

    public PurchaseService(BookService bookService, PurchaseRepository purchaseRepository) {
        this.bookService = bookService;
        this.purchaseRepository = purchaseRepository;
    }

    public void performBookPurchase(User user, Long bookId) {
        var book = bookService.findById(bookId);
        if (purchaseRepository.existsByUserIdAndBookId(user.id(), bookId)) {
            throw new IllegalArgumentException("The User has already bought this book");
        }
        var purchase = new BookPurchaseEntity(
                null,
                bookId,
                user.id(),
                Timestamp.from(Instant.now()),
                book.cost()
        );
        purchaseRepository.save(purchase);

        log.info("User has successfully purchased the book: userId={}, bookId={}", user.id(), bookId);
    }


}
