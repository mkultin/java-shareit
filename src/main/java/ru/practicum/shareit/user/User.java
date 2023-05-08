package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private Long id;
    private String name;
    @NotBlank(message = "Email не может быть пустым.")
    @Email(message = "Введен некорректный email.")
    private String email;
}
