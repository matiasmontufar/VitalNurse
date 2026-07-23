package com.proyecto.vitalnurse.entity.clinical;

import com.proyecto.vitalnurse.entity.catalogo.EstadoClinico;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clin_evaluacion_resultado")
public class EvaluacionResultado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idResultado;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cabecera", nullable = false, unique = true)
    private EvaluacionCabecera cabecera;

    @Column(name = "resultado_texto", nullable = false, length = 200)
    private String resultadoTexto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_clinico")
    private EstadoClinico estadoClinico;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public EvaluacionResultado() {}

    public Long getIdResultado() { return idResultado; }
    public void setIdResultado(Long idResultado) { this.idResultado = idResultado; }

    public EvaluacionCabecera getCabecera() { return cabecera; }
    public void setCabecera(EvaluacionCabecera cabecera) { this.cabecera = cabecera; }

    public String getResultadoTexto() { return resultadoTexto; }
    public void setResultadoTexto(String resultadoTexto) { this.resultadoTexto = resultadoTexto; }

    public EstadoClinico getEstadoClinico() { return estadoClinico; }
    public void setEstadoClinico(EstadoClinico estadoClinico) { this.estadoClinico = estadoClinico; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
