package com.demo.sqlite.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(description = "Signup Client Request DTO")
public class EmployeeSignupRequestDTO {
    @Schema(example = "Felipe")
    @NotBlank(message = "Field 'name' is required")
    private String name;
    @Schema(example = "Contreras")
    @NotBlank(message = "Field 'surnames' is required")
    private String surnames;
    @Schema(example = "felipe@example.com")
    @NotBlank(message = "Field 'email' is required")
    @Email(message = "Field 'email' must be a valid email address")
    private String email;
    @Schema(example = "ok")
    @NotBlank(message = "Field 'password' is required")
    private String password;

}
