package com.techxel.firstcaring.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A RapportSoignant.
 */
@Entity
@Table(name = "rapport_soignant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RapportSoignant implements Serializable {

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
    @Column(name = "facturation", nullable = false)
    private Double facturation;

    @Lob
    @Column(name = "description")
    private String description;

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

    public RapportSoignant codePatient(String codePatient) {
        this.codePatient = codePatient;
        return this;
    }

    public void setCodePatient(String codePatient) {
        this.codePatient = codePatient;
    }

    public String getCodePS() {
        return codePS;
    }

    public RapportSoignant codePS(String codePS) {
        this.codePS = codePS;
        return this;
    }

    public void setCodePS(String codePS) {
        this.codePS = codePS;
    }

    public Double getFacturation() {
        return facturation;
    }

    public RapportSoignant facturation(Double facturation) {
        this.facturation = facturation;
        return this;
    }

    public void setFacturation(Double facturation) {
        this.facturation = facturation;
    }

    public String getDescription() {
        return description;
    }

    public RapportSoignant description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public RapportSoignant createdAt(ZonedDateTime createdAt) {
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
        if (!(o instanceof RapportSoignant)) {
            return false;
        }
        return id != null && id.equals(((RapportSoignant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RapportSoignant{" +
            "id=" + getId() +
            ", codePatient='" + getCodePatient() + "'" +
            ", codePS='" + getCodePS() + "'" +
            ", facturation=" + getFacturation() +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            "}";
    }
}
