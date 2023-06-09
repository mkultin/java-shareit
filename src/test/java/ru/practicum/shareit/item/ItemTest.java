package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemTest {
    Item item = Item.builder()
            .id(1L)
            .name("Item")
            .description("description")
            .available(true)
            .build();

    Item item1 = Item.builder()
            .id(1L)
            .name("Item")
            .description("description")
            .available(true)
            .build();

    @Test
    void testItem() {
        assertEquals(item, item1);
        assertTrue(item.hashCode() == item1.hashCode());
    }
}
