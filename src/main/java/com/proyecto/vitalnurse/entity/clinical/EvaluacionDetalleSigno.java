package com.proyecto.vitalnurse.entity.clinical;

import com.proyecto.vitalnurse.entity.catalogo.CatSignoVital;
import com.proyecto.vitalnurse.entity.catalogo.UnidadMedida;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "clin_evaluacion_detalle_signo")
public class EvaluacionDetalleSigno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cabecera", nullable = false)
    private EvaluacionCabecera cabecera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_signo_vital", nullable = false)
    private CatSignoVital signoVital;

    @Column(name = "valor_medido", nullable = false, precision = 8, scale = 2)
    private BigDecimal valorMedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadMedida unidad;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public EvaluacionDetalleSigno() {}

    public Long getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Long idDetalle) { this.idDetalle = idDetalle; }

    public EvaluacionCabecera getCabecera() { return cabecera; }
    public void setCabecera(EvaluacionCabecera cabecera) { this.cabecera = cabecera; }

    public CatSignoVital getSignoVital() { return signoVital; }
    public void setSignoVital(CatSignoVital signoVital) { this.signoVital = signoVital; }

    public BigDecimal getValorMedido() { return valorMedido; }
    public void setValorMedido(BigDecimal valorMedido) { this.valorMedido = valorMedido; }

    public UnidadMedida getUnidad() { return unidad; }
    public void setUnidad(UnidadMedida unidad) { this.unidad = unidad; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
