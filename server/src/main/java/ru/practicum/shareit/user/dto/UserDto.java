package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.service.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = {Marker.Create.class})
    private String name;
    @NotBlank(message = "Email не может быть пустым.", groups = {Marker.Create.class})
    @Email(message = "Введен некорректный email.", groups = {Marker.Create.class, Marker.Update.class})
    private String email;
}