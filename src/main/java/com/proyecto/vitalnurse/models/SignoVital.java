package com.proyecto.vitalnurse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "signos_vitales")
public class SignoVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSigno;

    @NotNull(message = "La temperatura es obligatoria")
    @DecimalMin(value = "30.0", message = "Temperatura fuera de rango")
    @DecimalMax(value = "45.0", message = "Temperatura fuera de rango")
    @Column(nullable = false)
    private Double temperatura;

    @NotBlank(message = "La presión arterial es obligatoria")
    @Column(nullable = false)
    private String presionArterial;

    @NotNull(message = "La frecuencia cardíaca es obligatoria")
    @Min(value = 20, message = "FC fuera de rango")
    @Max(value = 300, message = "FC fuera de rango")
    @Column(nullable = false)
    private Integer frecuenciaCardiaca;

    @NotNull(message = "La frecuencia respiratoria es obligatoria")
    @Min(value = 4, message = "FR fuera de rango")
    @Max(value = 100, message = "FR fuera de rango")
    @Column(nullable = false)
    private Integer frecuenciaRespiratoria;

    @NotNull(message = "La saturación de oxígeno es obligatoria")
    @Min(value = 30, message = "SpO2 fuera de rango")
    @Max(value = 100, message = "SpO2 fuera de rango")
    @Column(nullable = false)
    private Integer saturacionOxigeno;

    @NotNull(message = "La glicemia es obligatoria")
    @DecimalMin(value = "10.0", message = "Glicemia fuera de rango")
    @DecimalMax(value = "600.0", message = "Glicemia fuera de rango")
    @Column(nullable = false)
    private Double glicemia;

    @NotNull(message = "La fecha es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    public SignoVital() {
    }

    public Long getIdSigno() { return idSigno; }
    public void setIdSigno(Long idSigno) { this.idSigno = idSigno; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }

    public Integer getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(Integer saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }

    public Double getGlicemia() { return glicemia; }
    public void setGlicemia(Double glicemia) { this.glicemia = glicemia; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public String getFechaFormateada() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return this.fecha.format(formatter);
    }
}
