package co.edu.unicauca.zonas_veredales.eventos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZonaEvent {
    private String action; // "created", "updated", "deleted"
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String departamento;
    private String municipio;
}
