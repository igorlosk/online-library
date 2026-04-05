package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.AbstractTest;
import dev.loskutnikov.onlinelibrary.books.*;
import dev.loskutnikov.onlinelibrary.users.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
class AuthorControllerTest extends AbstractTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;

    @Test
    void shouldSuccessCreateAuthor() throws Exception {
        var author = new AuthorDto(
                null,
                "author-name" + getRandomInt(),
                1900,
                List.of()
        );

        String authorJson = objectMapper.writeValueAsString(author);

        String createdAuthorJson = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authorJson)
                        .header("Authorization", getAuthorizationHeader(UserRole.ADMIN))
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var authorDtoResponse = objectMapper.readValue(createdAuthorJson, Author.class);

        Assertions.assertNotNull(authorDtoResponse.id());
        Assertions.assertEquals(authorDtoResponse.name(), author.name());

        Assertions.assertTrue(authorRepository.existsById(authorDtoResponse.id()));
    }

    @Test
    void shouldSuccessDeleteAuthor() throws Exception {
        var author = authorService.createAuthor(new Author(
                null,
                "author-name" + getRandomInt(),
                1900,
                List.of()
        ));
        List<Book> authorBooks = IntStream.range(0, 10)
                .mapToObj(i -> createBookToAuthor(author.id()))
                .toList();

        mockMvc.perform(
                        delete("/authors/{id}", author.id())
                                .header("Authorization", getAuthorizationHeader(UserRole.ADMIN))
                )
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        Assertions.assertFalse(authorRepository.existsById(author.id()));
        authorBooks.forEach(book -> {
            var updatedBook = bookService.findById(book.id());
            Assertions.assertNull(updatedBook.authorId());
        });
    }

    @Test
    void shouldReturnForbiddenWhenUserTriesToDeleteAuthor() throws Exception {
        mockMvc.perform(
                        delete("/authors/{id}", 1L)
                                .header("Authorization", getAuthorizationHeader(UserRole.USER))
                )
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    private Book createBookToAuthor(Long authorId) {
        return bookService.createBook(new Book(
                null,
                "some-book",
                authorId,
                2024,
                100,
                6000
        ));
    }
}

//class AuthorControllerTest extends AbstractTest {
//
//    @Autowired
//    private AuthorRepository authorRepository;
//    @Autowired
//    private AuthorService authorService;
//    @Autowired
//    private BookService bookService;
//
//    @Test
//    void shouldSuccessCreateAuthor() throws Exception {
//        // создаем автора
//        var author = new AuthorDto(
//                null,
//                "Author-name" + getRandomInt(),
//                1900,
//                List.of()
//        );
//        // переводим в JSON объект. Переводим author в строку
//        String authorJson = objectMapper.writeValueAsString(author);
//
//        // отправление POST запроса на сохранение этого author
//        // принимаемое значение createdAuthorJson
//        String createdAuthorJson = mockMvc.perform(post("/authors")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(authorJson)
//                        .header("Authorisation", getAuthorizationHeader(UserRole.ADMIN)))
//                .andExpect(status().is(201))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        // полученный createdAuthorJson (JSON) из контроллера преобразуем обратно в объект Author
//        Author authorDtoResponse = objectMapper.readValue(createdAuthorJson, Author.class);
//
//        // проверка, что у объекта проставился ID
//        Assertions.assertNotNull(authorDtoResponse.id());
//        // проверка, что имена совпадают
//        Assertions.assertEquals(authorDtoResponse.name(), author.name());
//        // проверка, что в базу данных сохранился этот автор
//        Assertions.assertTrue(authorRepository.existsById(authorDtoResponse.id()));
//    }
//
//    @Test
////    @WithMockUser(username = "admin", authorities = "ADMIN")
//    void shouldSuccessDeleteAuthor() throws Exception {
//        // создание автора
//        var author = authorService.createAuthor(new Author(
//                null,
//                "author-name" + getRandomInt(),
//                1900,
//                List.of()
//        ));
//        // сгенерируем для этого автора список книг
//        List<Book> authorBooks = IntStream.range(0, 10)
//                .mapToObj(i -> createBookToAuthor(author.id()))
//                .toList();
//
//        // с помощью MockMvc отправляю запрос на удаление из базы данных
//        mockMvc.perform(delete("/authors/{id}", author.id())
//                        .header("Authorisation", getAuthorizationHeader(UserRole.ADMIN)))
//                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));
//
//        // проверяем, что он действительно удалился
//        Assertions.assertFalse(authorRepository.existsById(author.id()));
//
//        // проверить у всех книг этого автора проставился author.id - null в базе данных
//        authorBooks.forEach(book -> {
//            var updatedBook = bookService.findById(book.id());
//            Assertions.assertNull(updatedBook.authorId());
//        });
//    }
//
//    // дополнительный метод по созданию книги
//    private Book createBookToAuthor(Long authorId) {
//        return bookService.createBook(new Book(
//                null,
//                "some-book",
//                authorId,
//                2024,
//                100,
//                6000
//        ));
//    }
//}