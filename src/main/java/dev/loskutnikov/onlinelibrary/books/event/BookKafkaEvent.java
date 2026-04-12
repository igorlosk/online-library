package dev.loskutnikov.onlinelibrary.books.event;

import dev.loskutnikov.onlinelibrary.books.Book;

public record BookKafkaEvent(
        Long bookId,
        EventType eventType,
        Book book
) {
}
