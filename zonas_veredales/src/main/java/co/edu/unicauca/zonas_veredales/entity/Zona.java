package co.edu.unicauca.zonas_veredales.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zonas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Zona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo; // e.g. "zona_norte"

    @Column(nullable = false)
    private String nombre; // "Zona Norte"

    private String descripcion;
    private String departamento;
    private String municipio;
}
