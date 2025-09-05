package com.LMS.library_management.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthorDTO {
    private Long id;

    @NotBlank(message = "Author name is required")
    private String name;

    private String biography;

}
