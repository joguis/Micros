package com.danielblanco.arquitecturasmodernas.microservices.user.controller;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.User;
import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("Pruebas Unitarias - UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private ZonaVeredal zonaVeredal;

    @BeforeEach
    void setUp() {
        zonaVeredal = new ZonaVeredal();
        zonaVeredal.setId(1L);
        zonaVeredal.setNombre("Zona Veredal Norte");
        zonaVeredal.setCodigo("ZV-NORTE");

        user = new User();
        user.setId(1L);
        user.setNombres("Juan");
        user.setApellidos("Pérez");
        user.setEmail("juan.perez@example.com");
        user.setTelefono("1234567890");
    }

    @Test
    @DisplayName("GET /{id} - Debería retornar un usuario cuando existe")
    void deberiaRetornarUsuarioCuandoExiste() throws Exception {
        // Given
        when(userService.findById(1L)).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombres").value("Juan"))
                .andExpect(jsonPath("$.apellidos").value("Pérez"))
                .andExpect(jsonPath("$.email").value("juan.perez@example.com"));

        verify(userService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /{id} - Debería retornar 404 cuando el usuario no existe")
    void deberiaRetornar404CuandoUsuarioNoExiste() throws Exception {
        // Given
        when(userService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("GET / - Debería retornar lista de usuarios")
    void deberiaRetornarListaDeUsuarios() throws Exception {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setNombres("María");
        user2.setApellidos("González");
        user2.setEmail("maria.gonzalez@example.com");

        List<User> usuarios = Arrays.asList(user, user2);
        when(userService.findAll()).thenReturn(usuarios);

        // When & Then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombres").value("Juan"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nombres").value("María"));

        verify(userService, times(1)).findAll();
    }

    @Test
    @DisplayName("POST / - Debería crear un usuario exitosamente")
    void deberiaCrearUsuarioExitosamente() throws Exception {
        // Given
        User nuevoUsuario = new User();
        nuevoUsuario.setNombres("Pedro");
        nuevoUsuario.setApellidos("Rodríguez");
        nuevoUsuario.setEmail("pedro.rodriguez@example.com");
        nuevoUsuario.setTelefono("0987654321");

        when(userService.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(3L);
            return u;
        });

        // When & Then
        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nombres").value("Pedro"))
                .andExpect(jsonPath("$.apellidos").value("Rodríguez"));

        verify(userService, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("PATCH /{userId}/asociar-zona-veredal - Debería asociar zona veredal exitosamente")
    void deberiaAsociarZonaVeredalExitosamente() throws Exception {
        // Given
        user.setZonaVeredal(zonaVeredal);
        Map<String, Long> request = new HashMap<>();
        request.put("zonaVeredalId", 1L);

        when(userService.asociarZonaVeredal(1L, 1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(patch("/{userId}/asociar-zona-veredal", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userService, times(1)).asociarZonaVeredal(1L, 1L);
    }

    @Test
    @DisplayName("PATCH /{userId}/asociar-zona-veredal - Debería retornar 400 cuando zonaVeredalId es null")
    void deberiaRetornar400CuandoZonaVeredalIdEsNull() throws Exception {
        // Given
        Map<String, Long> request = new HashMap<>();

        // When & Then
        mockMvc.perform(patch("/{userId}/asociar-zona-veredal", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).asociarZonaVeredal(any(), any());
    }

    @Test
    @DisplayName("PATCH /{userId}/asociar-zona-veredal - Debería retornar 400 cuando el campo zonaVeredalId no está presente")
    void deberiaRetornar400CuandoZonaVeredalIdNoEstaPresente() throws Exception {
        // Given
        Map<String, String> request = new HashMap<>();
        request.put("otroCampo", "valor");

        // When & Then
        mockMvc.perform(patch("/{userId}/asociar-zona-veredal", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).asociarZonaVeredal(any(), any());
    }
}