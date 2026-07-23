package com.proyecto.vitalnurse.entity.catalogo;

import jakarta.persistence.*;

@Entity
@Table(name = "cat_tipo_sangre")
public class TipoSangre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoSangre;

    @Column(nullable = false, unique = true, length = 3)
    private String codigo;

    @Column(nullable = false, length = 10)
    private String nombre;

    @Column(name = "factor_rh", nullable = false, length = 1)
    private String factorRh;

    public TipoSangre() {}

    public TipoSangre(Integer idTipoSangre) { this.idTipoSangre = idTipoSangre; }

    public Integer getIdTipoSangre() { return idTipoSangre; }
    public void setIdTipoSangre(Integer idTipoSangre) { this.idTipoSangre = idTipoSangre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFactorRh() { return factorRh; }
    public void setFactorRh(String factorRh) { this.factorRh = factorRh; }
}
