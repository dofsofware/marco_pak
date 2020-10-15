package com.techxel.firstcaring.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Pack.
 */
@Entity
@Table(name = "pack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "denommination", nullable = false)
    private String denommination;

    @NotNull
    @Column(name = "prix", nullable = false)
    private Double prix;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "nmb_de_pers", nullable = false)
    private Integer nmbDePers;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDenommination() {
        return denommination;
    }

    public Pack denommination(String denommination) {
        this.denommination = denommination;
        return this;
    }

    public void setDenommination(String denommination) {
        this.denommination = denommination;
    }

    public Double getPrix() {
        return prix;
    }

    public Pack prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public Pack description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNmbDePers() {
        return nmbDePers;
    }

    public Pack nmbDePers(Integer nmbDePers) {
        this.nmbDePers = nmbDePers;
        return this;
    }

    public void setNmbDePers(Integer nmbDePers) {
        this.nmbDePers = nmbDePers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pack)) {
            return false;
        }
        return id != null && id.equals(((Pack) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pack{" +
            "id=" + getId() +
            ", denommination='" + getDenommination() + "'" +
            ", prix=" + getPrix() +
            ", description='" + getDescription() + "'" +
            ", nmbDePers=" + getNmbDePers() +
            "}";
    }
}
