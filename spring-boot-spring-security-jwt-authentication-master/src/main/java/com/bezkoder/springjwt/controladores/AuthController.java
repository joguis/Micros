package com.bezkoder.springjwt.controladores;



import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.LoginRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.SignupRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.JwtResponseDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.MessageResponseDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.services.AuthImpl;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  
  @Autowired
  AuthImpl objAuthImpl;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {    
    System.out.println("entrando a generar token");
    JwtResponseDTO token=this.objAuthImpl.authenticateUser(loginRequest);    
    return ResponseEntity.ok(token);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequestDTO signUpRequest) {
    System.out.println("entrando a crear usuario");
    MessageResponseDTO mensajeRespuesta=this.objAuthImpl.registerUser(signUpRequest);
    return ResponseEntity.ok(mensajeRespuesta);
  }
}
