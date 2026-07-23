package com.proyecto.vitalnurse.entity.herramienta;

import com.proyecto.vitalnurse.entity.catalogo.UnidadMedida;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "her_auditoria_calculo_dosis")
public class AuditoriaCalculoDosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAuditoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(name = "dosis_indicada", nullable = false, precision = 10, scale = 2)
    private BigDecimal dosisIndicada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_dosis", nullable = false)
    private UnidadMedida unidadDosis;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal presentacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_unidad_presentacion", nullable = false)
    private UnidadMedida unidadPresentacion;

    @Column(name = "diluyente_ml", nullable = false, precision = 10, scale = 2)
    private BigDecimal diluyenteMl;

    @Column(name = "horas_totales", nullable = false, precision = 6, scale = 2)
    private BigDecimal horasTotales = BigDecimal.ZERO;

    @Column(name = "volumen_ml", precision = 10, scale = 2)
    private BigDecimal volumenMl;

    @Column(name = "ml_por_hora", precision = 10, scale = 2)
    private BigDecimal mlPorHora;

    @Column(name = "gotas_por_minuto", precision = 10, scale = 2)
    private BigDecimal gotasPorMinuto;

    @Column(name = "microgotas_por_minuto", precision = 10, scale = 2)
    private BigDecimal microgotasPorMinuto;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public AuditoriaCalculoDosis() {}

    public Long getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Long idAuditoria) { this.idAuditoria = idAuditoria; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public BigDecimal getDosisIndicada() { return dosisIndicada; }
    public void setDosisIndicada(BigDecimal dosisIndicada) { this.dosisIndicada = dosisIndicada; }

    public UnidadMedida getUnidadDosis() { return unidadDosis; }
    public void setUnidadDosis(UnidadMedida unidadDosis) { this.unidadDosis = unidadDosis; }

    public BigDecimal getPresentacion() { return presentacion; }
    public void setPresentacion(BigDecimal presentacion) { this.presentacion = presentacion; }

    public UnidadMedida getUnidadPresentacion() { return unidadPresentacion; }
    public void setUnidadPresentacion(UnidadMedida unidadPresentacion) { this.unidadPresentacion = unidadPresentacion; }

    public BigDecimal getDiluyenteMl() { return diluyenteMl; }
    public void setDiluyenteMl(BigDecimal diluyenteMl) { this.diluyenteMl = diluyenteMl; }

    public BigDecimal getHorasTotales() { return horasTotales; }
    public void setHorasTotales(BigDecimal horasTotales) { this.horasTotales = horasTotales; }

    public BigDecimal getVolumenMl() { return volumenMl; }
    public void setVolumenMl(BigDecimal volumenMl) { this.volumenMl = volumenMl; }

    public BigDecimal getMlPorHora() { return mlPorHora; }
    public void setMlPorHora(BigDecimal mlPorHora) { this.mlPorHora = mlPorHora; }

    public BigDecimal getGotasPorMinuto() { return gotasPorMinuto; }
    public void setGotasPorMinuto(BigDecimal gotasPorMinuto) { this.gotasPorMinuto = gotasPorMinuto; }

    public BigDecimal getMicrogotasPorMinuto() { return microgotasPorMinuto; }
    public void setMicrogotasPorMinuto(BigDecimal microgotasPorMinuto) { this.microgotasPorMinuto = microgotasPorMinuto; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
