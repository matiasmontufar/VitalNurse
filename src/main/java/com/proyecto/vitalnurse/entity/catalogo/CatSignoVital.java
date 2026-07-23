package com.proyecto.vitalnurse.entity.catalogo;

import com.proyecto.vitalnurse.entity.audit.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "cat_signo_vital")
public class CatSignoVital extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSigno;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 200)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadMedida unidad;

    @Column(name = "rango_min")
    private Double rangoMin;

    @Column(name = "rango_max")
    private Double rangoMax;

    @Column(nullable = false)
    private Integer orden = 0;

    public CatSignoVital() {}

    public CatSignoVital(Long idSigno) { this.idSigno = idSigno; }

    public Long getIdSigno() { return idSigno; }
    public void setIdSigno(Long idSigno) { this.idSigno = idSigno; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public UnidadMedida getUnidad() { return unidad; }
    public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }

    public Double getRangoMin() { return rangoMin; }
    public void setRangoMin(Double rangoMin) { this.rangoMin = rangoMin; }

    public Double getRangoMax() { return rangoMax; }
    public void setRangoMax(Double rangoMax) { this.rangoMax = rangoMax; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}
