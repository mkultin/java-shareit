package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
