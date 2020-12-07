package com.techxel.firstcaring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Assure.
 */
@Entity
@Table(name = "assure")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Assure implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "code_assure", length = 20, nullable = false, unique = true)
    private String codeAssure;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "profil", nullable = false)
    private Profil profil;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Size(min = 7)
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "created_at", nullable = false)
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

    @ManyToOne
    @JsonIgnoreProperties(value = "assures", allowSetters = true)
    private Assureur assureur;

    @ManyToOne
    @JsonIgnoreProperties(value = "assures", allowSetters = true)
    private Pack pack;

    @ManyToMany(mappedBy = "assures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Assureur> assureurs = new HashSet<>();

    @ManyToMany(mappedBy = "assures")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<PS> ps = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeAssure() {
        return codeAssure;
    }

    public Assure codeAssure(String codeAssure) {
        this.codeAssure = codeAssure;
        return this;
    }

    public void setCodeAssure(String codeAssure) {
        this.codeAssure = codeAssure;
    }

    public Profil getProfil() {
        return profil;
    }

    public Assure profil(Profil profil) {
        this.profil = profil;
        return this;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Assure sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public Assure telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Assure createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public Assure urlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getAdresse() {
        return adresse;
    }

    public Assure adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public User getUser() {
        return user;
    }

    public Assure user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assureur getAssureur() {
        return assureur;
    }

    public Assure assureur(Assureur assureur) {
        this.assureur = assureur;
        return this;
    }

    public void setAssureur(Assureur assureur) {
        this.assureur = assureur;
    }

    public Pack getPack() {
        return pack;
    }

    public Assure pack(Pack pack) {
        this.pack = pack;
        return this;
    }

    public void setPack(Pack pack) {
        this.pack = pack;
    }

    public Set<Assureur> getAssureurs() {
        return assureurs;
    }

    public Assure assureurs(Set<Assureur> assureurs) {
        this.assureurs = assureurs;
        return this;
    }

    public Assure addAssureur(Assureur assureur) {
        this.assureurs.add(assureur);
        assureur.getAssures().add(this);
        return this;
    }

    public Assure removeAssureur(Assureur assureur) {
        this.assureurs.remove(assureur);
        assureur.getAssures().remove(this);
        return this;
    }

    public void setAssureurs(Set<Assureur> assureurs) {
        this.assureurs = assureurs;
    }

    public Set<PS> getPs() {
        return ps;
    }

    public Assure ps(Set<PS> pS) {
        this.ps = pS;
        return this;
    }

    public Assure addPs(PS pS) {
        this.ps.add(pS);
        pS.getAssures().add(this);
        return this;
    }

    public Assure removePs(PS pS) {
        this.ps.remove(pS);
        pS.getAssures().remove(this);
        return this;
    }

    public void setPs(Set<PS> pS) {
        this.ps = pS;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Assure)) {
            return false;
        }
        return id != null && id.equals(((Assure) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Assure{" +
            "id=" + getId() +
            ", codeAssure='" + getCodeAssure() + "'" +
            ", profil='" + getProfil() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", urlPhoto='" + getUrlPhoto() + "'" +
            ", adresse='" + getAdresse() + "'" +
            "}";
    }
}
