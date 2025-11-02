package com.bezkoder.springjwt.configuracionSeguridad.fachada.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.entidades.ERole;
import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.entidades.Role;
import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.entidades.User;
import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.repositorios.RoleRepository;
import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.repositorios.UserRepository;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.LoginRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.request.SignupRequestDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.JwtResponseDTO;
import com.bezkoder.springjwt.configuracionSeguridad.fachada.DTO.response.MessageResponseDTO;
import com.bezkoder.springjwt.configuracionSeguridad.security.jwt.JwtUtils;
import com.bezkoder.springjwt.configuracionSeguridad.security.services.UserDetailsImpl;


@Service
public class AuthImpl implements AuthInt{

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;


    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        return new JwtResponseDTO(jwt, 
        userDetails.getId(), 
        userDetails.getUsername(), 
        userDetails.getEmail(), 
        roles);
    }

    @Override
    public MessageResponseDTO registerUser(SignupRequestDTO signUpRequest) {
       
        User user = new User(signUpRequest.getUsername(),signUpRequest.getEmail(),encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                    break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole); 

                    break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
        return new MessageResponseDTO("Usuario "+user.getUsername()+" creado exitosamente");
    }

}
