package dev.loskutnikov.onlinelibrary.books.purchase;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "user_books_purchases")
public class BookPurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_id")
    private Long bookId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "date")
    private Timestamp purchaseDate;

    @Column(name = "cost")
    private Integer purchaseCost;

    public BookPurchaseEntity() {
    }

    public BookPurchaseEntity(Long id, Long bookId, Long userId, Timestamp purchaseDate, Integer purchaseCost) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.purchaseCost = purchaseCost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public Integer getPurchaseCost() {
        return purchaseCost;
    }

    public void setPurchaseCost(Integer purchaseCost) {
        this.purchaseCost = purchaseCost;
    }
}
