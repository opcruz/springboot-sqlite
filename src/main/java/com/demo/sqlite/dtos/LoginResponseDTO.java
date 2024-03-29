package com.demo.sqlite.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class LoginResponseDTO {

    private String email;
    private String token;
    private String type = "Bearer";

}
