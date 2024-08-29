package com.demo.sqlite.service;

import com.demo.sqlite.exception.ValidationError;
import com.demo.sqlite.model.dto.request.ClientSignupRequestDTO;
import com.demo.sqlite.model.dto.request.EmployeeSignupRequestDTO;
import com.demo.sqlite.model.dto.request.LoginRequestDTO;
import com.demo.sqlite.model.dto.response.LoginResponseDTO;
import com.demo.sqlite.model.entity.Client;
import com.demo.sqlite.model.entity.Employee;

import java.util.Optional;

public interface UserService {

   Optional<LoginResponseDTO> login(LoginRequestDTO requestDTO, String role) throws ValidationError;

   Client signupClient(ClientSignupRequestDTO requestDTO) throws ValidationError;

   Employee signupEmployee(EmployeeSignupRequestDTO requestDTO) throws ValidationError;

}
