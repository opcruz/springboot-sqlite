package com.demo.sqlite.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class LoginResponseDTO {
   private String email;
   private String token;
   @Builder.Default
   private String type = "Bearer";

}
