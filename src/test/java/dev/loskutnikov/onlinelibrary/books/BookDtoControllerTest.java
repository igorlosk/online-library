package dev.loskutnikov.onlinelibrary.books;

import dev.loskutnikov.onlinelibrary.AbstractTest;
import dev.loskutnikov.onlinelibrary.author.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookDtoControllerTest extends AbstractTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldSuccessCreateBook() throws Exception {
        Author author = createDummeAuthor();
        var book = new BookDto(
                null,
                "some-book",
                author.id(),
                2026,
                100,
                6000
        );
        // нужно перевести book в JSON
        String bookJson = objectMapper.writeValueAsString(book);

        String createdBookJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto bookDtoResponse = objectMapper.readValue(createdBookJson, BookDto.class);

        Assertions.assertNotNull(bookDtoResponse.id());
        Assertions.assertEquals(book.name(), bookDtoResponse.name());
        Assertions.assertTrue(bookRepository.existsById(bookDtoResponse.id()));

    }



    @Test
    void shouldNotCreateBookWhenRequestNotValid() throws Exception {

        var book = new BookDto(
                null,
                null,
                1L,
                2026,
                100,
                6000
        );
        // нужно перевести book в JSON
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().is(400));


    }

    @Test
    void shouldSuccessSearchBookById() throws Exception {
        var author = createDummeAuthor();
        var book = new Book(
                null,
                "BOOK",
                author.id(),
                2026,
                100,
                6000
        );

        book = bookService.createBook(book);

        String foundBookJson = mockMvc.perform(get("/books/{id}", book.id()))
                .andExpect(status().is(200))
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto foundBookDto = objectMapper.readValue(foundBookJson, BookDto.class);

        org.assertj.core.api.Assertions
                .assertThat(book)
                .usingRecursiveComparison().isEqualTo(foundBookDto);
    }

    @Test
    void shouldReturnNotFoundWhenBookNotPresent() throws Exception {

        mockMvc.perform(get("/books/{id}", Integer.MAX_VALUE))
                .andExpect(status().is(404));

    }

    private Author createDummeAuthor() {
        return authorService.createAuthor(new Author(
                null,
                "author-name" + getRandomInt(),
                1900,
                List.of()
        ));
    }

}