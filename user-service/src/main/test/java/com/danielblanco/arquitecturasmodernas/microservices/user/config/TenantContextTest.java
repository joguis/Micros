package com.danielblanco.arquitecturasmodernas.microservices.user.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias - TenantContext")
class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("Debería establecer y obtener el tenant actual")
    void deberiaEstablecerYObtenerTenantActual() {
        // Given
        Long zonaVeredalId = 1L;

        // When
        TenantContext.setCurrentTenant(zonaVeredalId);

        // Then
        assertEquals(zonaVeredalId, TenantContext.getCurrentTenant());
    }

    @Test
    @DisplayName("Debería retornar null cuando no hay tenant establecido")
    void deberiaRetornarNullCuandoNoHayTenantEstablecido() {
        // When
        Long resultado = TenantContext.getCurrentTenant();

        // Then
        assertNull(resultado);
    }

    @Test
    @DisplayName("Debería limpiar el tenant después de clear()")
    void deberiaLimpiarTenantDespuesDeClear() {
        // Given
        TenantContext.setCurrentTenant(1L);
        assertNotNull(TenantContext.getCurrentTenant());

        // When
        TenantContext.clear();

        // Then
        assertNull(TenantContext.getCurrentTenant());
    }

    @Test
    @DisplayName("Debería actualizar el tenant cuando se establece uno nuevo")
    void deberiaActualizarTenantCuandoSeEstableceUnoNuevo() {
        // Given
        TenantContext.setCurrentTenant(1L);
        assertEquals(1L, TenantContext.getCurrentTenant());

        // When
        TenantContext.setCurrentTenant(2L);

        // Then
        assertEquals(2L, TenantContext.getCurrentTenant());
    }

    @Test
    @DisplayName("Debería manejar múltiples threads independientemente")
    void deberiaManejarMultiplesThreadsIndependientemente() throws InterruptedException {
        // Given
        TenantContext.setCurrentTenant(1L);

        // When
        Thread thread = new Thread(() -> {
            TenantContext.setCurrentTenant(2L);
            assertEquals(2L, TenantContext.getCurrentTenant());
        });
        thread.start();
        thread.join();

        // Then - El thread principal mantiene su valor
        assertEquals(1L, TenantContext.getCurrentTenant());
    }
}