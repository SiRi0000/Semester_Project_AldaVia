package org.hbrs.se2.project.aldavia.entities;

import java.util.*;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "kenntnisse", schema = "aldavia_new")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Kenntnis {
    @Id
    @Column(name = "bezeichnung")
    private String bezeichnung;

    @ManyToMany(mappedBy = "kenntnisse")
    private List<Student> students;

    public Kenntnis addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        if (!this.students.contains(student)) {
            this.students.add(student);
            student.addKenntnis(this);
        }
        return this;
    }

    public Kenntnis removeStudent(Student student) {
        if (students == null) {
            return this;
        }
        if (this.students.contains(student)) {
            this.students.remove(student);
            student.removeKenntnis(this);
        }
        return this;
    }
}