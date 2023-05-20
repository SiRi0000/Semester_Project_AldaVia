package org.hbrs.se2.project.aldavia.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
// import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table( name ="sprache" , schema = "carlook" )
@NoArgsConstructor
@Setter
@Getter
public class Sprache {
    private String name;
    private String level;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private int spracheId;

    @Basic
    @Column(name = "name", nullable = false)
    public String getName() { return name; }

    @Basic
    @Column(name = "level", nullable = false)
    public String getLevel() { return level; }


    @ManyToMany(mappedBy = "sprachen")
    private List<Student> studenten;
    public List<Student> getStudenten() {
        return studenten;
    }

    public void addStudent(Student student) {
        if (studenten == null)
            studenten = new ArrayList<>();
        if (this.studenten.contains(student))
            return;
        studenten.add(student);
        student.addSprache(this);
    }

    public void removeStudent(Student student) {
        if (studenten == null)
            studenten = new ArrayList<>();
        if (!this.studenten.contains(student))
            return;
        studenten.remove(student);
        student.removeSprache(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprache sprache = (Sprache) o;
        return Objects.equals(name, sprache.name) && Objects.equals(level, sprache.level) && Objects.equals(spracheId, sprache.spracheId) && Objects.equals(studenten, sprache.studenten);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, spracheId, studenten);
    }
}
