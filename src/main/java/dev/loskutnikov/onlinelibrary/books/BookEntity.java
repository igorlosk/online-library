package dev.loskutnikov.onlinelibrary.books;


import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "pub_year", nullable = false)
    private Integer publicationYear;

    @Column(name = "page_number", nullable = false)
    private Integer pageNumber;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    public BookEntity() {
    }

    public BookEntity(Long id, String name, Long authorId, Integer publicationYear, Integer pageNumber, Integer cost) {
        this.id = id;
        this.name = name;
        this.authorId = authorId;
        this.publicationYear = publicationYear;
        this.pageNumber = pageNumber;
        this.cost = cost;
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

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }
}
