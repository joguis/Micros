package co.edu.unicauca.usuario_service.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;

@Entity
@Table(name = "zonas_local")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ZonaLocal {
    @Id
    private Long id; // usar el mismo id externo
    @Column(unique = true, nullable = false)
    private String codigo;
    private String nombre;
    private String descripcion;
    private String departamento;
    private String municipio;
}
