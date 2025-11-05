package com.danielblanco.arquitecturasmodernas.microservices.user.controller;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.service.ZonaVeredalService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ZonaVeredalController.class)
@DisplayName("Pruebas Unitarias - ZonaVeredalController")
class ZonaVeredalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ZonaVeredalService zonaVeredalService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("GET /zonas-veredales/ - Debería retornar lista de zonas veredales")
    void deberiaRetornarListaDeZonasVeredales() throws Exception {
        // Given
        List<ZonaVeredal> zonasVeredales = Arrays.asList(zonaVeredal1, zonaVeredal2);
        when(zonaVeredalService.findAll()).thenReturn(zonasVeredales);

        // When & Then
        mockMvc.perform(get("/zonas-veredales/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nombre").value("Zona Veredal Norte"))
                .andExpect(jsonPath("$[0].codigo").value("ZV-NORTE"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].nombre").value("Zona Veredal Sur"));

        verify(zonaVeredalService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /zonas-veredales/{id} - Debería retornar una zona veredal cuando existe")
    void deberiaRetornarZonaVeredalCuandoExiste() throws Exception {
        // Given
        when(zonaVeredalService.findById(1L)).thenReturn(Optional.of(zonaVeredal1));

        // When & Then
        mockMvc.perform(get("/zonas-veredales/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nombre").value("Zona Veredal Norte"))
                .andExpect(jsonPath("$.codigo").value("ZV-NORTE"))
                .andExpect(jsonPath("$.descripcion").value("Zona veredal ubicada en la región norte"));

        verify(zonaVeredalService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET /zonas-veredales/{id} - Debería lanzar excepción cuando la zona veredal no existe")
    void deberiaLanzarExcepcionCuandoZonaVeredalNoExiste() throws Exception {
        // Given
        when(zonaVeredalService.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/zonas-veredales/{id}", 999L))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$").doesNotExist()); // RuntimeException causa error 500

        verify(zonaVeredalService, times(1)).findById(999L);
    }

    @Test
    @DisplayName("POST /zonas-veredales/ - Debería crear una zona veredal exitosamente")
    void deberiaCrearZonaVeredalExitosamente() throws Exception {
        // Given
        ZonaVeredal nuevaZona = new ZonaVeredal();
        nuevaZona.setNombre("Zona Veredal Centro");
        nuevaZona.setCodigo("ZV-CENTRO");
        nuevaZona.setDescripcion("Zona veredal ubicada en la región centro");

        when(zonaVeredalService.save(any(ZonaVeredal.class))).thenAnswer(invocation -> {
            ZonaVeredal zv = invocation.getArgument(0);
            zv.setId(3L);
            return zv;
        });

        // When & Then
        mockMvc.perform(post("/zonas-veredales/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaZona)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.nombre").value("Zona Veredal Centro"))
                .andExpect(jsonPath("$.codigo").value("ZV-CENTRO"));

        verify(zonaVeredalService, times(1)).save(any(ZonaVeredal.class));
    }
}