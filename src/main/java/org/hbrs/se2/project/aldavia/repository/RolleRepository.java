package org.hbrs.se2.project.aldavia.repository;

import org.aspectj.apache.bcel.classfile.Module;
import org.hbrs.se2.project.aldavia.entities.Rolle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
/**
 * JPA-Repository für die Abfrage der Rollen von registrierten User. Die Bezeichnung einer Methode
 * bestimmt dabei die Selektionsbedingung (den WHERE-Teil). Der Rückgabewert einer
 * Methode bestimmt den Projectionsbedingung (den SELECT-Teil).
 * Mehr Information über die Entwicklung von Queries in JPA:
 * https://www.baeldung.com/spring-data-jpa-projections
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
 *
 */
public interface RolleRepository extends JpaRepository<Rolle, String> {

    Optional<Rolle> findByBezeichnung(String bezeichnung);

}