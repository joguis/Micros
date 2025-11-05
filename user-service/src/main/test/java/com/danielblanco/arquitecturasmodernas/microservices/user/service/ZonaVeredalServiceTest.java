package com.danielblanco.arquitecturasmodernas.microservices.user.service;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.repository.ZonaVeredalRepository;
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
@DisplayName("Pruebas Unitarias - ZonaVeredalService")
class ZonaVeredalServiceTest {

    @Mock
    private ZonaVeredalRepository zonaVeredalRepository;

    @InjectMocks
    private ZonaVeredalService zonaVeredalService;

    private ZonaVeredal zonaVeredal1;
    private ZonaVeredal zonaVeredal2;

    @BeforeEach
    void setUp() {
        zonaVeredal1 = new ZonaVeredal();
        zonaVeredal1.setId(1L);
        zonaVeredal1.setNombre("Zona Veredal Norte");
        zonaVeredal1.setCodigo("ZV-NORTE");
        zonaVeredal1.setDescripcion("Zona veredal ubicada en la región norte");

        zonaVeredal2 = new ZonaVeredal();
        zonaVeredal2.setId(2L);
        zonaVeredal2.setNombre("Zona Veredal Sur");
        zonaVeredal2.setCodigo("ZV-SUR");
        zonaVeredal2.setDescripcion("Zona veredal ubicada en la región sur");
    }

    @Test
    @DisplayName("Debería encontrar todas las zonas veredales")
    void deberiaEncontrarTodasLasZonasVeredales() {
        // Given
        List<ZonaVeredal> zonasVeredales = Arrays.asList(zonaVeredal1, zonaVeredal2);
        when(zonaVeredalRepository.findAll()).thenReturn(zonasVeredales);

        // When
        List<ZonaVeredal> resultado = zonaVeredalService.findAll();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Zona Veredal Norte", resultado.get(0).getNombre());
        assertEquals("Zona Veredal Sur", resultado.get(1).getNombre());
        verify(zonaVeredalRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería encontrar una zona veredal por ID")
    void deberiaEncontrarZonaVeredalPorId() {
        // Given
        when(zonaVeredalRepository.findById(1L)).thenReturn(Optional.of(zonaVeredal1));

        // When
        Optional<ZonaVeredal> resultado = zonaVeredalService.findById(1L);

        // Then
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Zona Veredal Norte", resultado.get().getNombre());
        assertEquals("ZV-NORTE", resultado.get().getCodigo());
        verify(zonaVeredalRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debería retornar Optional vacío cuando la zona veredal no existe")
    void deberiaRetornarOptionalVacioCuandoZonaVeredalNoExiste() {
        // Given
        when(zonaVeredalRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<ZonaVeredal> resultado = zonaVeredalService.findById(999L);

        // Then
        assertFalse(resultado.isPresent());
        verify(zonaVeredalRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debería guardar una zona veredal")
    void deberiaGuardarZonaVeredal() {
        // Given
        ZonaVeredal nuevaZona = new ZonaVeredal();
        nuevaZona.setNombre("Zona Veredal Centro");
        nuevaZona.setCodigo("ZV-CENTRO");
        nuevaZona.setDescripcion("Zona veredal ubicada en la región centro");

        when(zonaVeredalRepository.save(any(ZonaVeredal.class))).thenAnswer(invocation -> {
            ZonaVeredal zv = invocation.getArgument(0);
            zv.setId(3L);
            return zv;
        });

        // When
        ZonaVeredal resultado = zonaVeredalService.save(nuevaZona);

        // Then
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Zona Veredal Centro", resultado.getNombre());
        assertEquals("ZV-CENTRO", resultado.getCodigo());
        verify(zonaVeredalRepository, times(1)).save(nuevaZona);
    }
}