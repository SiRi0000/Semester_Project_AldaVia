package org.hbrs.se2.project.aldavia.control;

import org.hbrs.se2.project.aldavia.control.exception.PersistenceException;
import org.hbrs.se2.project.aldavia.dtos.SpracheDTO;
import org.hbrs.se2.project.aldavia.entities.Sprache;
import org.hbrs.se2.project.aldavia.entities.Student;
import org.hbrs.se2.project.aldavia.repository.SprachenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
@Transactional
public class SprachenControl {
    @Autowired
    private SprachenRepository sprachenRepository;

    /**
     * Create a new Sprache or return an existing one
     * @param spracheDTO The SpracheDTO
     * @return Sprache
     */
    public Sprache addStudentToSprache (SpracheDTO spracheDTO, Student student) throws PersistenceException {
        Optional<Sprache> awaitSprache = sprachenRepository.findById(spracheDTO.getId());
        Sprache sprache;
        if (awaitSprache.isPresent()){
            sprache = awaitSprache.get();
        }
        else {
            sprache = Sprache.builder()
                    .bezeichnung(spracheDTO.getName())
                    .level(spracheDTO.getLevel())
                    .build();
        }
        sprache = sprache.addStudent(student);
        return sprachenRepository.save(sprache);
    }

    /**
     * Remove a student from a Sprache and save it
     * @param spracheDTO The SpracheDTO
     * @param student The student
     * @throws PersistenceException If the Sprache is not found
     */
    public Sprache removeStudentFromSprache(SpracheDTO spracheDTO, Student student) throws PersistenceException {
        Optional<Sprache> awaitSprache = sprachenRepository.findById(spracheDTO.getId());
        if (awaitSprache.isPresent()){
            Sprache sprache = awaitSprache.get();
            sprache = sprache.removeStudent(student);
            return sprachenRepository.save(sprache);
        }
        else {
            throw new PersistenceException(PersistenceException.PersistenceExceptionType.SpracheNotFound, "Sprache not found");
        }
    }
}
