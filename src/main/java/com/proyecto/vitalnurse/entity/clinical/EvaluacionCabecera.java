package com.proyecto.vitalnurse.entity.clinical;

import com.proyecto.vitalnurse.entity.audit.Auditable;
import com.proyecto.vitalnurse.entity.catalogo.TipoEvaluacion;
import com.proyecto.vitalnurse.entity.persona.Paciente;
import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "clin_evaluacion_cabecera")
public class EvaluacionCabecera extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCabecera;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_evaluacion", nullable = false)
    private TipoEvaluacion tipoEvaluacion;

    @Column(nullable = false)
    private LocalDateTime fechaHora = LocalDateTime.now();

    @OneToOne(mappedBy = "cabecera", fetch = FetchType.LAZY)
    private EvaluacionResultado resultado;

    @OneToMany(mappedBy = "cabecera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EvaluacionDetalleSigno> detalles = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String notas;

    public EvaluacionCabecera() {}

    public EvaluacionCabecera(Long idCabecera) { this.idCabecera = idCabecera; }

    public Long getIdCabecera() { return idCabecera; }
    public void setIdCabecera(Long idCabecera) { this.idCabecera = idCabecera; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public TipoEvaluacion getTipoEvaluacion() { return tipoEvaluacion; }
    public void setTipoEvaluacion(TipoEvaluacion tipoEvaluacion) { this.tipoEvaluacion = tipoEvaluacion; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public EvaluacionResultado getResultado() { return resultado; }
    public void setResultado(EvaluacionResultado resultado) { this.resultado = resultado; }

    public Set<EvaluacionDetalleSigno> getDetalles() { return detalles; }
    public void setDetalles(Set<EvaluacionDetalleSigno> detalles) { this.detalles = detalles; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
