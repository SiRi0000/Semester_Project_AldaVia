package org.hbrs.se2.project.aldavia.dtos.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hbrs.se2.project.aldavia.dtos.StudentProfileDTO;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StudentProfileDTOImpl implements StudentProfileDTO {

    private String vorname;
    private String nachname;
    private String matrikelNummer;
    private String studiengang;
    private LocalDate studienbeginn;
    private LocalDate geburtsdatum;
    private String email;
}
