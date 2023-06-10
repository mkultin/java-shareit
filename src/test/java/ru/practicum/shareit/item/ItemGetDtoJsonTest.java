package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemGetDto;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.Entities.itemGetDto;

@JsonTest
public class ItemGetDtoJsonTest {

    @Autowired
    private JacksonTester<ItemGetDto> json;

    @Test
    void testItemGetDto() throws Exception {

        JsonContent<ItemGetDto> result = json.write(itemGetDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemGetDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemGetDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
    }
}
