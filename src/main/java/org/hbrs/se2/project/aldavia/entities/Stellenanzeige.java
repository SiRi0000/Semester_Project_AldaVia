package org.hbrs.se2.project.aldavia.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stellenanzeige", schema = "carlook")
@NoArgsConstructor
@Getter
@Setter
public class Stellenanzeige {
    @Id
    @GeneratedValue
    private int stellenanzeigeId;

    @Basic
    @Column(name = "bezeichnung")
    private String bezeichnung;

    @Basic
    @Column(name = "start")
    private LocalDate start;

    @Basic
    @Column(name = "ende")
    private LocalDate ende;

    @Basic
    @Column(name = "erstellungsdatum")
    private LocalDate erstellungsdatum;

    @Basic
    @Column(name = "bezahlung")
    private String bezahlung;

    @Basic
    @Column(name = "beschaeftigungsumfang")
    private String beschaeftigungsumfang;

    @Basic
    @Column(name = "beschreibung")
    private String beschreibung;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stellenanzeige stellenanzeige = (Stellenanzeige) o;
        return stellenanzeigeId == stellenanzeige.stellenanzeigeId &&
                Objects.equals(bezeichnung, stellenanzeige.bezeichnung) &&
                Objects.equals(beschreibung, stellenanzeige.beschreibung) &&
                Objects.equals(start, stellenanzeige.start) &&
                Objects.equals(ende, stellenanzeige.ende) &&
                Objects.equals(erstellungsdatum, stellenanzeige.erstellungsdatum) &&
                Objects.equals(bezahlung, stellenanzeige.bezahlung) &&
                Objects.equals(beschaeftigungsumfang, stellenanzeige.beschaeftigungsumfang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stellenanzeigeId, bezeichnung, beschreibung, start, ende, erstellungsdatum, bezahlung, beschaeftigungsumfang);
    }

    // unternehmen_erstellt_stellenanzeige
    @ManyToOne
    private Unternehmen unternehmen;
    public Unternehmen getUnternehmen() { return unternehmen; }
    public void setUnternehmen(Unternehmen unternehmen) {this.unternehmen = unternehmen;}

    // stellenanzeige_hat_taetigkeitsfeld
    @ManyToMany
    private List<Taetigkeitsfeld> taetigkeitsfelder;
    @JoinTable(name = "stellenanzeige_hat_taetigkeitsfeld", catalog = "nmuese2s", schema = "carlook",
            joinColumns = @JoinColumn(name = "stellenanzeige_id", referencedColumnName = "stellenanzeigeId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "taetigkeitsfeld", referencedColumnName = "bezeichnung", nullable = false))
    public List<Taetigkeitsfeld> getTaetigkeitsfelder() {
        return taetigkeitsfelder;
    }

    // stellenanzeige_hat_bewerbung
    @OneToMany
    private List<Bewerbung> bewerbungen;
    @JoinTable(name = "stellenanzeige_hat_bewerbung", catalog = "nmuese2s", schema = "carlook",
            joinColumns = @JoinColumn(name = "stellenanzeige_id", referencedColumnName = "stellenanzeigeId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "bewerbung_id", referencedColumnName = "bewerbungId", nullable = false))
    public List<Bewerbung> getBewerbung() {
        return bewerbungen;
    }
    public void setBewerbung(List<Bewerbung> bewerbungen) {
        this.bewerbungen = bewerbungen;
    }

}
