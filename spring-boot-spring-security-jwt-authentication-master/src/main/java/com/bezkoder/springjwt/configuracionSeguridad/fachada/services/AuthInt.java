package com.bezkoder.springjwt.configuracionSeguridad.fachada.services;


import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.LoginRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.SignupRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.JwtResponseDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.MessageResponseDTO;


public interface AuthInt {
   JwtResponseDTO authenticateUser( LoginRequestDTO loginRequest);
   MessageResponseDTO registerUser(SignupRequestDTO signUpRequest);
}
