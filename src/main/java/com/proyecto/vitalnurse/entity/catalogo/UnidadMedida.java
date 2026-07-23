package com.proyecto.vitalnurse.entity.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "cat_unidad_medida")
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUnidad;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 30)
    private String nombre;

    @Column(length = 10)
    private String simbolo;

    @Column(nullable = false, length = 20)
    private String categoria;

    public UnidadMedida() {}

    public UnidadMedida(Long idUnidad) { this.idUnidad = idUnidad; }

    public Long getIdUnidad() { return idUnidad; }
    public void setIdUnidad(Long idUnidad) { this.idUnidad = idUnidad; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSimbolo() { return simbolo; }
    public void setSimbolo(String simbolo) { this.simbolo = simbolo; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
