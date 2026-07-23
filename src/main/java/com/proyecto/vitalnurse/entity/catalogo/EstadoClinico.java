package com.proyecto.vitalnurse.entity.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "cat_estado_clinico")
public class EstadoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_signo")
    private CatSignoVital signo;

    public EstadoClinico() {}

    public EstadoClinico(Long idEstado) { this.idEstado = idEstado; }

    public Long getIdEstado() { return idEstado; }
    public void setIdEstado(Long idEstado) { this.idEstado = idEstado; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public CatSignoVital getSigno() { return signo; }
    public void setSigno(CatSignoVital signo) { this.signo = signo; }
}
