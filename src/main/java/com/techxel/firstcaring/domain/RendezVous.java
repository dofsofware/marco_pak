package com.techxel.firstcaring.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A RendezVous.
 */
@Entity
@Table(name = "rendez_vous")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RendezVous implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "code_patient", length = 20, nullable = false)
    private String codePatient;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "code_ps", length = 20, nullable = false)
    private String codePS;

    @NotNull
    @Column(name = "debut_rv", nullable = false)
    private ZonedDateTime debutRV;

    @NotNull
    @Column(name = "fin_rv", nullable = false)
    private ZonedDateTime finRV;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePatient() {
        return codePatient;
    }

    public RendezVous codePatient(String codePatient) {
        this.codePatient = codePatient;
        return this;
    }

    public void setCodePatient(String codePatient) {
        this.codePatient = codePatient;
    }

    public String getCodePS() {
        return codePS;
    }

    public RendezVous codePS(String codePS) {
        this.codePS = codePS;
        return this;
    }

    public void setCodePS(String codePS) {
        this.codePS = codePS;
    }

    public ZonedDateTime getDebutRV() {
        return debutRV;
    }

    public RendezVous debutRV(ZonedDateTime debutRV) {
        this.debutRV = debutRV;
        return this;
    }

    public void setDebutRV(ZonedDateTime debutRV) {
        this.debutRV = debutRV;
    }

    public ZonedDateTime getFinRV() {
        return finRV;
    }

    public RendezVous finRV(ZonedDateTime finRV) {
        this.finRV = finRV;
        return this;
    }

    public void setFinRV(ZonedDateTime finRV) {
        this.finRV = finRV;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public RendezVous createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RendezVous)) {
            return false;
        }
        return id != null && id.equals(((RendezVous) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RendezVous{" +
            "id=" + getId() +
            ", codePatient='" + getCodePatient() + "'" +
            ", codePS='" + getCodePS() + "'" +
            ", debutRV='" + getDebutRV() + "'" +
            ", finRV='" + getFinRV() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
