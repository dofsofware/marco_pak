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

/**
 * A PS.
 */
@Entity
@Table(name = "ps")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PS implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 20)
    @Column(name = "code_ps", length = 20, nullable = false, unique = true)
    private String codePS;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "profil", nullable = false)
    private Profil profil;

    @NotNull
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "url_photo")
    private String urlPhoto;

    @NotNull
    @Size(min = 4)
    @Column(name = "profession", nullable = false)
    private String profession;

    @NotNull
    @Max(value = 30)
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @NotNull
    @Size(min = 4, max = 200)
    @Column(name = "nom_de_letablissement", length = 200, nullable = false)
    private String nomDeLetablissement;

    @NotNull
    @Column(name = "telephone_de_letablissement", nullable = false)
    private String telephoneDeLetablissement;

    @NotNull
    @Size(min = 4, max = 200)
    @Column(name = "adresse_de_letablissement", length = 200, nullable = false)
    private String adresseDeLetablissement;

    @Size(min = 100, max = 600)
    @Column(name = "lien_google_maps_de_letablissement", length = 600)
    private String lienGoogleMapsDeLetablissement;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "ps_assure",
               joinColumns = @JoinColumn(name = "ps_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "assure_id", referencedColumnName = "id"))
    private Set<Assure> assures = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodePS() {
        return codePS;
    }

    public PS codePS(String codePS) {
        this.codePS = codePS;
        return this;
    }

    public void setCodePS(String codePS) {
        this.codePS = codePS;
    }

    public Profil getProfil() {
        return profil;
    }

    public PS profil(Profil profil) {
        this.profil = profil;
        return this;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public String getTelephone() {
        return telephone;
    }

    public PS telephone(String telephone) {
        this.telephone = telephone;
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public PS createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public PS urlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getProfession() {
        return profession;
    }

    public PS profession(String profession) {
        this.profession = profession;
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Integer getExperience() {
        return experience;
    }

    public PS experience(Integer experience) {
        this.experience = experience;
        return this;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public String getNomDeLetablissement() {
        return nomDeLetablissement;
    }

    public PS nomDeLetablissement(String nomDeLetablissement) {
        this.nomDeLetablissement = nomDeLetablissement;
        return this;
    }

    public void setNomDeLetablissement(String nomDeLetablissement) {
        this.nomDeLetablissement = nomDeLetablissement;
    }

    public String getTelephoneDeLetablissement() {
        return telephoneDeLetablissement;
    }

    public PS telephoneDeLetablissement(String telephoneDeLetablissement) {
        this.telephoneDeLetablissement = telephoneDeLetablissement;
        return this;
    }

    public void setTelephoneDeLetablissement(String telephoneDeLetablissement) {
        this.telephoneDeLetablissement = telephoneDeLetablissement;
    }

    public String getAdresseDeLetablissement() {
        return adresseDeLetablissement;
    }

    public PS adresseDeLetablissement(String adresseDeLetablissement) {
        this.adresseDeLetablissement = adresseDeLetablissement;
        return this;
    }

    public void setAdresseDeLetablissement(String adresseDeLetablissement) {
        this.adresseDeLetablissement = adresseDeLetablissement;
    }

    public String getLienGoogleMapsDeLetablissement() {
        return lienGoogleMapsDeLetablissement;
    }

    public PS lienGoogleMapsDeLetablissement(String lienGoogleMapsDeLetablissement) {
        this.lienGoogleMapsDeLetablissement = lienGoogleMapsDeLetablissement;
        return this;
    }

    public void setLienGoogleMapsDeLetablissement(String lienGoogleMapsDeLetablissement) {
        this.lienGoogleMapsDeLetablissement = lienGoogleMapsDeLetablissement;
    }

    public User getUser() {
        return user;
    }

    public PS user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Assure> getAssures() {
        return assures;
    }

    public PS assures(Set<Assure> assures) {
        this.assures = assures;
        return this;
    }

    public PS addAssure(Assure assure) {
        this.assures.add(assure);
        assure.getPs().add(this);
        return this;
    }

    public PS removeAssure(Assure assure) {
        this.assures.remove(assure);
        assure.getPs().remove(this);
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
        if (!(o instanceof PS)) {
            return false;
        }
        return id != null && id.equals(((PS) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PS{" +
            "id=" + getId() +
            ", codePS='" + getCodePS() + "'" +
            ", profil='" + getProfil() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", urlPhoto='" + getUrlPhoto() + "'" +
            ", profession='" + getProfession() + "'" +
            ", experience=" + getExperience() +
            ", nomDeLetablissement='" + getNomDeLetablissement() + "'" +
            ", telephoneDeLetablissement='" + getTelephoneDeLetablissement() + "'" +
            ", adresseDeLetablissement='" + getAdresseDeLetablissement() + "'" +
            ", lienGoogleMapsDeLetablissement='" + getLienGoogleMapsDeLetablissement() + "'" +
            "}";
    }
}
