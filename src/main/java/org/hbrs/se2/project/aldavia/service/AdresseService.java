package org.hbrs.se2.project.aldavia.service;

import lombok.SneakyThrows;
import org.hbrs.se2.project.aldavia.control.exception.PersistenceException;
import org.hbrs.se2.project.aldavia.dtos.AdresseDTO;
import org.hbrs.se2.project.aldavia.entities.Adresse;
import org.hbrs.se2.project.aldavia.entities.Unternehmen;
import org.hbrs.se2.project.aldavia.repository.AdresseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional
public class AdresseService {
    @Autowired
    private AdresseRepository addressenRepository;
    private final Logger logger = LoggerFactory.getLogger(AdresseService.class);

    public Adresse addUnternehmenToAdresse(AdresseDTO adresseDTO, Unternehmen unternehmen) {
        logger.info("Adding unternehmen " + unternehmen.getUser().getUserid() + " to " + adresseDTO.getId());
        Optional<Adresse> awaitAdresse = addressenRepository.findById(adresseDTO.getId());
        Adresse adresse;
        adresse = awaitAdresse.orElseGet(() -> Adresse.builder()
                .id(adresseDTO.getId())
                .strasse(adresseDTO.getStrasse())
                .hausnummer(adresseDTO.getHausnummer())
                .plz(adresseDTO.getPlz())
                .ort(adresseDTO.getOrt())
                .land(adresseDTO.getLand())
                .build());
        adresse = adresse.addUnternehmen(unternehmen);
        unternehmen.addAdresse(adresse);
        System.out.println("Adresse size nach addUnternehmen: " + unternehmen.getAdressen().size());
        return addressenRepository.save(adresse);
    }

    @SneakyThrows
    public Adresse removeUnternehmenFromAdresse(AdresseDTO adresse, Unternehmen unternehmen) {
        logger.info("Removing student from Adresse");
        Optional<Adresse> awaitAdresse = addressenRepository.findById(adresse.getId());
        if (awaitAdresse.isPresent()) {
            Adresse adresse1 = awaitAdresse.get();
            adresse1 = adresse1.removeUnternehmen(unternehmen);
            addressenRepository.delete(adresse1);
            return adresse1;
        } else {
            throw new PersistenceException(PersistenceException.PersistenceExceptionType.SPRACHE_NOT_FOUND, "Adresse not found");
        }
    }
    @SneakyThrows
    public Adresse removeUnternehmenFromAdresse(Adresse adresse, Unternehmen unternehmen) {
        logger.info("Removing student from Adresse");
        Optional<Adresse> awaitAdresse = addressenRepository.findById(adresse.getId());
        if (awaitAdresse.isPresent()) {
            Adresse adresse1 = awaitAdresse.get();
            adresse1 = adresse1.removeUnternehmen(unternehmen);
            addressenRepository.delete(adresse1);
            return adresse1;
        } else {
            throw new PersistenceException(PersistenceException.PersistenceExceptionType.SPRACHE_NOT_FOUND, "Adresse not found");
        }
    }


}
