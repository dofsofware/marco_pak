package com.techxel.firstcaring.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.techxel.firstcaring.domain.enumeration.Profil;

import com.techxel.firstcaring.domain.enumeration.Sexe;

/**
 * A Assureur.
 */
@Entity
@Table(name = "assureur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assureur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "code_assureur", length = 20, nullable = false, unique = true)
    private String codeAssureur;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "profil", nullable = true)
    private Profil profil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "created_at", nullable = true)
    private ZonedDateTime createdAt;

    @Column(name = "url_photo")
    private String urlPhoto;

    @NotNull
    @Size(min = 4, max = 200)
    @Column(name = "adresse", length = 200, nullable = false)
    private String adresse;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "assureur_assure",
               joinColumns = @JoinColumn(name = "assureur_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "assure_id", referencedColumnName = "id"))
    private Set<Assure> assures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssureur() {
        return codeAssureur;
    }

    public Assureur codeAssureur(String codeAssureur) {
        this.codeAssureur = codeAssureur;
        return this;
    }

    public void setCodeAssureur(String codeAssureur) {
        this.codeAssureur = codeAssureur;
    }

    public Profil getProfil() {
        return profil;
    }

    public Assureur profil(Profil profil) {
        this.profil = profil;
        return this;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Assureur sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public Assureur telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Assureur createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public Assureur urlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getAdresse() {
        return adresse;
    }

    public Assureur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public User getUser() {
        return user;
    }

    public Assureur user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Assure> getAssures() {
        return assures;
    }

    public Assureur assures(Set<Assure> assures) {
        this.assures = assures;
        return this;
    }

    public Assureur addAssure(Assure assure) {
        this.assures.add(assure);
        assure.getAssureurs().add(this);
        return this;
    }

    public Assureur removeAssure(Assure assure) {
        this.assures.remove(assure);
        assure.getAssureurs().remove(this);
        return this;
    }

    public void setAssures(Set<Assure> assures) {
        this.assures = assures;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assureur)) {
            return false;
        }
        return id != null && id.equals(((Assureur) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assureur{" +
            "id=" + getId() +
            ", codeAssureur='" + getCodeAssureur() + "'" +
            ", profil='" + getProfil() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", urlPhoto='" + getUrlPhoto() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
