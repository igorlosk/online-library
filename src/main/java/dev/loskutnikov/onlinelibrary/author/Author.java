package dev.loskutnikov.onlinelibrary.author;

import dev.loskutnikov.onlinelibrary.books.*;

import java.util.List;

public record Author(
        Long id,
        String name,
        Integer birthYear,
        List<Book> books
) {
}
