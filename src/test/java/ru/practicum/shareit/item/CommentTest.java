package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentTest {
    private final Comment comment = Comment.builder()
            .id(1L)
            .text("text")
            .created(LocalDateTime.of(1111, 1, 1, 1, 1))
            .build();

    private final Comment comment1 = Comment.builder()
            .id(1L)
            .text("text")
            .created(LocalDateTime.of(1111, 1, 1, 1, 1))
            .build();

    @Test
    void testCommentEqualsAndHashcode() {
        assertEquals(comment1, comment);
        assertTrue(comment.hashCode() == comment1.hashCode());
    }
}
