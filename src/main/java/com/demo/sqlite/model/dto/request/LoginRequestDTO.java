package com.demo.sqlite.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(description = "Login Request DTO")
public class LoginRequestDTO {
    @Schema(example = "john@example.com")
    @NotBlank(message = "Field 'email' is required")
    String email;
    @Schema(example = "ok")
    @NotBlank(message = "Field 'password' is required")
    String password;

}
