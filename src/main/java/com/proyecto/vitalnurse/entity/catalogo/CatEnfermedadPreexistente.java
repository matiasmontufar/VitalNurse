package com.proyecto.vitalnurse.entity.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "cat_enfermedad_preexistente")
public class CatEnfermedadPreexistente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnfermedad;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public CatEnfermedadPreexistente() {}

    public CatEnfermedadPreexistente(Long idEnfermedad) { this.idEnfermedad = idEnfermedad; }

    public Long getIdEnfermedad() { return idEnfermedad; }
    public void setIdEnfermedad(Long idEnfermedad) { this.idEnfermedad = idEnfermedad; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
