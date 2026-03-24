package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.books.BookEntity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "authors")
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Integer bornYear;


    // один автор много книг
    @OneToMany
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    // author_id находится в сущности Книг
    // id - на какую колонку ссылается author_id
    private Set<BookEntity> books;

    public AuthorEntity() {
    }

    public AuthorEntity(Long id, String name, Integer bornYear, Set<BookEntity> books) {
        this.id = id;
        this.name = name;
        this.bornYear = bornYear;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBornYear() {
        return bornYear;
    }

    public void setBornYear(Integer bornYear) {
        this.bornYear = bornYear;
    }

    public Set<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(Set<BookEntity> books) {
        this.books = books;
    }
}
