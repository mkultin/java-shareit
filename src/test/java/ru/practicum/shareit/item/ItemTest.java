package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.shareit.Entities.item;

public class ItemTest {

    private final Item sameItem = Item.builder()
            .id(1L)
            .name("Item")
            .description("description")
            .available(true)
            .build();

    @Test
    void testItem() {
        assertEquals(item, sameItem);
        assertTrue(item.hashCode() == sameItem.hashCode());
    }
}
