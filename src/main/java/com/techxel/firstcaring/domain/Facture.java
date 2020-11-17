package com.techxel.firstcaring.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Facture.
 */
@Entity
@Table(name = "facture")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Facture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Column(name = "prix", nullable = false)
    private Double prix;

    @ManyToOne
    @JsonIgnoreProperties(value = "factures", allowSetters = true)
    private PS ps;

    @ManyToOne
    @JsonIgnoreProperties(value = "factures", allowSetters = true)
    private Assureur assureur;

    @ManyToOne
    @JsonIgnoreProperties(value = "factures", allowSetters = true)
    private Assure assure;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Facture date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public Double getPrix() {
        return prix;
    }

    public Facture prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public PS getPs() {
        return ps;
    }

    public Facture ps(PS pS) {
        this.ps = pS;
        return this;
    }

    public void setPs(PS pS) {
        this.ps = pS;
    }

    public Assureur getAssureur() {
        return assureur;
    }

    public Facture assureur(Assureur assureur) {
        this.assureur = assureur;
        return this;
    }

    public void setAssureur(Assureur assureur) {
        this.assureur = assureur;
    }

    public Assure getAssure() {
        return assure;
    }

    public Facture assure(Assure assure) {
        this.assure = assure;
        return this;
    }

    public void setAssure(Assure assure) {
        this.assure = assure;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Facture)) {
            return false;
        }
        return id != null && id.equals(((Facture) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Facture{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", prix=" + getPrix() +
            "}";
    }
}
