package com.demo.sqlite.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Schema(description = "Signup Client Request DTO")
public class ClientSignupRequestDTO {
   @Schema(example = "Carlos")
   @NotBlank(message = "Field 'name' is required")
   private String name;
   @Schema(example = "Cruz")
   @NotBlank(message = "Field 'surnames' is required")
   private String surnames;
   @Schema(example = "Av. Principal 123")
   @NotBlank(message = "Field 'direction' is required")
   private String direction;
   @Schema(example = "Jalisco")
   @NotBlank(message = "Field 'state' is required")
   private String state;
   @Schema(example = "45000")
   @NotBlank(message = "Field 'postal_code' is required")
   private String postal_code;
   @Schema(example = "3300000000")
   @NotBlank(message = "Field 'phone' is required")
   private String phone;
   @Schema(example = "carlos@example.com")
   @NotBlank(message = "Field 'email' is required")
   @Email(message = "Field 'email' must be a valid email address")
   private String email;
   @Schema(example = "ok")
   @NotBlank(message = "Field 'password' is required")
   private String password;

}
