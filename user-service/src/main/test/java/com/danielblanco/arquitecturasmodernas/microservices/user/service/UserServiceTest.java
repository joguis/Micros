package com.danielblanco.arquitecturasmodernas.microservices.user.service;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.User;
import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ZonaVeredalService zonaVeredalService;

    @InjectMocks
    private UserService userService;

    private User user;
    private ZonaVeredal zonaVeredal;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setNombres("Juan");
        user.setApellidos("Pérez");
        user.setEmail("juan.perez@example.com");
        user.setTelefono("1234567890");

        zonaVeredal = new ZonaVeredal();
        zonaVeredal.setId(1L);
        zonaVeredal.setNombre("Zona Veredal Norte");
        zonaVeredal.setCodigo("ZV-NORTE");
        zonaVeredal.setDescripcion("Zona veredal ubicada en la región norte");
    }

    @Test
    @DisplayName("Debería encontrar un usuario por ID")
    void deberiaEncontrarUsuarioPorId() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        Optional<User> resultado = userService.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Juan", resultado.get().getNombres());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando el usuario no existe")
    void deberiaRetornarOptionalVacioCuandoUsuarioNoExiste() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<User> resultado = userService.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería encontrar todos los usuarios")
    void deberiaEncontrarTodosLosUsuarios() {
        // Given
        User user2 = new User();
        user2.setId(2L);
        user2.setNombres("María");
        user2.setApellidos("González");
        user2.setEmail("maria.gonzalez@example.com");

        List<User> usuarios = Arrays.asList(user, user2);
        when(userRepository.findAll()).thenReturn(usuarios);

        // When
        List<User> resultado = userService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombres());
        assertEquals("María", resultado.get(1).getNombres());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería guardar un usuario")
    void deberiaGuardarUsuario() {
        // Given
        User nuevoUsuario = new User();
        nuevoUsuario.setNombres("Pedro");
        nuevoUsuario.setApellidos("Rodríguez");
        nuevoUsuario.setEmail("pedro.rodriguez@example.com");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(3L);
            return u;
        });

        // When
        User resultado = userService.save(nuevoUsuario);

        // Then
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Pedro", resultado.getNombres());
        verify(userRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    @DisplayName("Debería asociar una zona veredal a un usuario exitosamente")
    void deberiaAsociarZonaVeredalAUsuario() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(zonaVeredalService.findById(1L)).thenReturn(Optional.of(zonaVeredal));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User resultado = userService.asociarZonaVeredal(1L, 1L);

        // Then
        assertNotNull(resultado);
        assertEquals(zonaVeredal, resultado.getZonaVeredal());
        verify(userRepository, times(1)).findById(1L);
        verify(zonaVeredalService, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el usuario no existe al asociar zona veredal")
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.asociarZonaVeredal(999L, 1L);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository, times(1)).findById(999L);
        verify(zonaVeredalService, never()).findById(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la zona veredal no existe al asociar")
    void deberiaLanzarExcepcionCuandoZonaVeredalNoExiste() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(zonaVeredalService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.asociarZonaVeredal(1L, 999L);
        });

        assertEquals("Zona Veredal no encontrada", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(zonaVeredalService, times(1)).findById(999L);
        verify(userRepository, never()).save(any());
    }
}