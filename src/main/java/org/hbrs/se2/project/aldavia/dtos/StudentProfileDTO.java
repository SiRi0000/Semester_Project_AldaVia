package org.hbrs.se2.project.aldavia.dtos;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StudentProfileDTO extends ProfileDTO {
    String getVorname();
    String getNachname();
    String getMatrikelNummer();
    String getStudiengang();
    LocalDate getStudienbeginn();
    LocalDate getGeburtsdatum();
    String getEmail();

    List<KenntnisDTO> getKenntnisse();

    List<QualifikationsDTO> getQualifikationen();

    List<SpracheDTO> getSprachen();

    List<TaetigkeitsfeldDTO> getTaetigkeitsfelder();
}
