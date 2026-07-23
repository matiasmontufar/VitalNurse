package com.proyecto.vitalnurse.entity.persona;

import com.proyecto.vitalnurse.entity.audit.Auditable;
import com.proyecto.vitalnurse.entity.catalogo.CatEnfermedadPreexistente;
import com.proyecto.vitalnurse.entity.catalogo.TipoSangre;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "per_paciente")
public class Paciente extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false, unique = true)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_sangre")
    private TipoSangre tipoSangre;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @ManyToMany
    @JoinTable(name = "per_paciente_enfermedad",
               joinColumns = @JoinColumn(name = "id_paciente"),
               inverseJoinColumns = @JoinColumn(name = "id_enfermedad"))
    private Set<CatEnfermedadPreexistente> enfermedades = new HashSet<>();

    public Paciente() {}

    public Paciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public TipoSangre getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(TipoSangre tipoSangre) { this.tipoSangre = tipoSangre; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public Set<CatEnfermedadPreexistente> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(Set<CatEnfermedadPreexistente> enfermedades) { this.enfermedades = enfermedades; }
}
