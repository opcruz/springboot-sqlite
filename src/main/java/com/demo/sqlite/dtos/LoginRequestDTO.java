package com.demo.sqlite.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(description = "Login Request DTO")
public class LoginRequestDTO {
    @Schema(example = "john@example.com")
    String email;
    @Schema(example = "ok")
    String password;

}
