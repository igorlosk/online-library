package dev.loskutnikov.onlinelibrary;

import dev.loskutnikov.onlinelibrary.books.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class BookDtoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateBook() throws Exception {

        var book = new BookDto(
                null,
                "BOOK",
                1L,
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
        var book = new BookDto(
                null,
                "BOOK",
                1L,
                2026,
                100,
                6000
        );

//        book = bookService.createBook(book);

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


}