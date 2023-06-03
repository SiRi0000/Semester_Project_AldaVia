package org.hbrs.se2.project.aldavia.test.ProfileTest;

import org.hbrs.se2.project.aldavia.control.*;
import org.hbrs.se2.project.aldavia.control.exception.PersistenceException;
import org.hbrs.se2.project.aldavia.control.exception.ProfileException;
import org.hbrs.se2.project.aldavia.dtos.*;
import org.hbrs.se2.project.aldavia.entities.*;
import org.hbrs.se2.project.aldavia.repository.StudentRepository;
import org.hbrs.se2.project.aldavia.service.KenntnisseService;
import org.hbrs.se2.project.aldavia.service.SprachenService;
import org.hbrs.se2.project.aldavia.service.StudentService;
import org.hbrs.se2.project.aldavia.service.TaetigkeitsfeldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ProfileControlTest {
    @Autowired
    private StudentProfileControl studentProfileControl;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private KenntnisseService kenntnisseService;

    @Autowired
    private SprachenService sprachenService;

    @Autowired
    private TaetigkeitsfeldService taetigkeitsfeldService;

    private Student student;
    private ChangeStudentInformationDTO changeStudentInformationDTO;
    private DeletionStudentInformationDTO deletionStudentInformationDTO;

    private AddStudentInformationDTO addStudentInformationDTO;
    private Kenntnis kenntnis;
    private Taetigkeitsfeld taetigkeitsfeld;
    private Sprache sprache;
    private Qualifikation qualifikation;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .userid("MaxMüller2001")
                .password("TestPassword")
                .email("MaxMüller2001@aldavia.de")
                .beschreibung("Ich bin ein Student.")
                .phone("0123456789")
                .build();

        student = Student.builder()
                .vorname("Max")
                .nachname("Müller")
                .geburtsdatum(LocalDate.of(2001, 1, 1))
                .studiengang("Informatik")
                .studienbeginn(LocalDate.of(2020, 1, 1))
                .matrikelNummer("12345678")
                .lebenslauf("Ich bin ein Student.")
                .build();

        student.setUser(user);

        KenntnisDTO kenntnisDTO = KenntnisDTO.builder()
                .name("Java")
                .build();

        TaetigkeitsfeldDTO taetigkeitsfeldDTO = TaetigkeitsfeldDTO.builder()
                .name("Software Entwicklung")
                .build();

        SpracheDTO spracheDTO = SpracheDTO.builder()
                .name("Englisch")
                .level("C1")
                .build();

        qualifikation = Qualifikation.builder()
                .beschreibung("Ich habe ein Praktikum bei Aldavia absolviert.")
                .bereich("Software Entwicklung")
                .bezeichnung("SaaS Entwickler")
                .institution("Aldavia GmbH")
                .von(LocalDate.of(2020, 1, 1))
                .bis(LocalDate.of(2020, 7, 1))
                .beschaftigungsverhaltnis("Praktikum")
                .build();

        kenntnis = kenntnisseService.getKenntnis(kenntnisDTO);
        taetigkeitsfeld = taetigkeitsfeldService.getTaetigkeitsfeld(taetigkeitsfeldDTO);
        sprache = sprachenService.getSprache(spracheDTO);

        student.addKenntnis(kenntnis);
        student.addTaetigkeitsfeld(taetigkeitsfeld);
        student.addQualifikation(qualifikation);
        student.addSprache(sprache);

        student = studentRepository.save(student);

        changeStudentInformationDTO = new ChangeStudentInformationDTO();
        addStudentInformationDTO = new AddStudentInformationDTO();
        deletionStudentInformationDTO = new DeletionStudentInformationDTO();
    }

    @AfterEach
    void tearDown() {
        try {
            studentService.deleteStudent(student);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetStudent() throws ProfileException {
        StudentProfileDTO studentProfileDTO = studentProfileControl.getStudentProfile(student.getUser().getUserid());

        // Assert Student Information
        assertEquals(studentProfileDTO.getVorname(), student.getVorname());
        assertEquals(studentProfileDTO.getNachname(), student.getNachname());
        assertEquals(studentProfileDTO.getGeburtsdatum(), student.getGeburtsdatum());
        assertEquals(studentProfileDTO.getStudiengang(), student.getStudiengang());
        assertEquals(studentProfileDTO.getStudienbeginn(), student.getStudienbeginn());
        assertEquals(studentProfileDTO.getMatrikelNummer(), student.getMatrikelNummer());
        assertEquals(studentProfileDTO.getLebenslauf(), student.getLebenslauf());


        // Assert User Information
        assertEquals(studentProfileDTO.getBeschreibung(), student.getUser().getBeschreibung());
        assertEquals(studentProfileDTO.getKenntnisse().get(0).getName(), student.getKenntnisse().get(0).getBezeichnung());
        assertEquals(studentProfileDTO.getTelefonnummer(), student.getUser().getPhone());

        // Assert Qualifikation Information
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschreibung(), student.getQualifikationen().get(0).getBeschreibung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBereich(), student.getQualifikationen().get(0).getBereich());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBezeichnung(), student.getQualifikationen().get(0).getBezeichnung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getInstitution(), student.getQualifikationen().get(0).getInstitution());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getVon(), student.getQualifikationen().get(0).getVon());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBis(), student.getQualifikationen().get(0).getBis());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschaeftigungsart(), student.getQualifikationen().get(0).getBeschaftigungsverhaltnis());

        // Assert Sprache Information
        assertEquals(studentProfileDTO.getSprachen().get(0).getName(), student.getSprachen().get(0).getBezeichnung());
        assertEquals(studentProfileDTO.getSprachen().get(0).getLevel(), student.getSprachen().get(0).getLevel());

        // Assert Taetigkeitsfeld Information
        assertEquals(studentProfileDTO.getTaetigkeitsfelder().get(0).getName(), student.getTaetigkeitsfelder().get(0).getBezeichnung());

        // Assert Kenntnis Information
        assertEquals(studentProfileDTO.getKenntnisse().get(0).getName(), student.getKenntnisse().get(0).getBezeichnung());
    }

    @Test
    public void testChangeStudentInformation() throws ProfileException, PersistenceException {
        Student student = studentRepository.findByUserID("MaxMüller2001").get();
        System.out.println("Student:" + student.getUser().getUserid());

        changeStudentInformationDTO = ChangeStudentInformationDTO.builder()
                .geburtsdatum(LocalDate.of(2001, 1, 1))
                .studiengang("Wirtschaftsinformatik")
                .studienbeginn(LocalDate.of(2020, 1, 1))
                .matrikelnummer("9012305678")
                .lebenslauf("Ich bin der größe Sascha Alda Fan")
                .beschreibung("Ich bin ein toller Student.")
                .telefonnummer("0124123456789")
                .build();

        // To add
        KenntnisDTO kenntnisDTOAdd = KenntnisDTO.builder()
                .name("C++")
                .build();

        SpracheDTO spracheDTOAdd = SpracheDTO.builder()
                .name("Französisch")
                .level("B2")
                .build();

        QualifikationsDTO qualifikationDTOAdd = QualifikationsDTO.builder()
                .beschreibung("Ich habe ein Praktikum bei No Code absolviert. Es hat mir nicht gefallen Adalvia war defintiv besser. Spaß :)")
                .bereich("Software Entwicklung")
                .bezeichnung("SaaS Entwickler")
                .institution("No Code GmbH")
                .von(LocalDate.of(2020, 8, 1))
                .bis(LocalDate.of(2020, 8, 5))
                .beschaeftigungsart("Praktikum")
                .id(-1)
                .build();

        TaetigkeitsfeldDTO taetigkeitsfeldDTOAdd = TaetigkeitsfeldDTO.builder()
                .name("Software Design")
                .build();

        // Create Lists
        List<TaetigkeitsfeldDTO> addTaetigkeitsfelder = new ArrayList<>();
        List<SpracheDTO> addSprachen = new ArrayList<>();
        List<QualifikationsDTO> addQulifikationen = new ArrayList<>();
        List<KenntnisDTO> addKenntnisse = new ArrayList<>();


        // Add to Lists
        addTaetigkeitsfelder.add(taetigkeitsfeldDTOAdd);
        addSprachen.add(spracheDTOAdd);
        addQulifikationen.add(qualifikationDTOAdd);
        addKenntnisse.add(kenntnisDTOAdd);


        /*// Build DeletionStudentInformationDTO
        deletionStudentInformationDTO.setKenntnisse(null);
        deletionStudentInformationDTO.setQualifikationen(null);
        deletionStudentInformationDTO.setSprachen(null);
        deletionStudentInformationDTO.setTaetigkeitsfelder(null);*/

        // Build AddStudentInformationDTO
        addStudentInformationDTO.setKenntnisse(addKenntnisse);
        addStudentInformationDTO.setQualifikationen(addQulifikationen);
        addStudentInformationDTO.setSprachen(addSprachen);
        addStudentInformationDTO.setTaetigkeitsfelder(addTaetigkeitsfelder);

        // Build UpdateStudentInformationDTO
        UpdateStudentProfileDTO updateStudentProfileDTO = UpdateStudentProfileDTO.builder()
                .addStudentInformationDTO(addStudentInformationDTO)
                .changeStudentInformationDTO(changeStudentInformationDTO)
                .deletionStudentInformationDTO(deletionStudentInformationDTO)
                .build();

        StudentProfileDTO newstudentProfileDTO = StudentProfileDTO.builder()
                .email("sina.schmidt@aldavia-mail.de")
                .vorname("Sina")
                .nachname("Schmidt")
                .geburtsdatum(LocalDate.of(2001, 1, 1))
                .studiengang("Wirtschaftsinformatik")
                .studienbeginn(LocalDate.of(2020, 1, 1))
                .matrikelNummer("9012305678")
                .lebenslauf("Ich bin der größe Sascha Alda Fan")
                .beschreibung("Ich bin ein toller Student.")
                .telefonnummer("0124123456789")
                .kenntnisse(addKenntnisse)
                .sprachen(addSprachen)
                .qualifikationen(addQulifikationen)
                .taetigkeitsfelder(addTaetigkeitsfelder)
                .build();


        studentProfileControl.updateStudentProfile(newstudentProfileDTO, student.getUser().getUserid());


        StudentProfileDTO studentProfileDTO = studentProfileControl.getStudentProfile(student.getUser().getUserid());

        // Assert Student Information
        assertEquals(studentProfileDTO.getVorname(), student.getVorname());
        assertEquals(studentProfileDTO.getNachname(), student.getNachname());
        assertEquals(studentProfileDTO.getGeburtsdatum(), changeStudentInformationDTO.getGeburtsdatum());
        assertEquals(studentProfileDTO.getStudiengang(), changeStudentInformationDTO.getStudiengang());
        assertEquals(studentProfileDTO.getStudienbeginn(), changeStudentInformationDTO.getStudienbeginn(), "Studienbeginn ist nicht gleich");
        assertEquals(studentProfileDTO.getMatrikelNummer(), changeStudentInformationDTO.getMatrikelnummer());
        assertEquals(studentProfileDTO.getLebenslauf(), changeStudentInformationDTO.getLebenslauf());

        // Assert User Information
        assertEquals(studentProfileDTO.getBeschreibung(), changeStudentInformationDTO.getBeschreibung());
        assertEquals(studentProfileDTO.getTelefonnummer(), changeStudentInformationDTO.getTelefonnummer());

        // Assert Qualifikation Information
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschreibung(), addStudentInformationDTO.getQualifikationen().get(0).getBeschreibung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBereich(), addStudentInformationDTO.getQualifikationen().get(0).getBereich());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBezeichnung(), addStudentInformationDTO.getQualifikationen().get(0).getBezeichnung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getInstitution(), addStudentInformationDTO.getQualifikationen().get(0).getInstitution());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getVon(), addStudentInformationDTO.getQualifikationen().get(0).getVon());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBis(), addStudentInformationDTO.getQualifikationen().get(0).getBis());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschaeftigungsart(), addStudentInformationDTO.getQualifikationen().get(0).getBeschaeftigungsart());

        // Assert Sprache Information
        assertEquals(studentProfileDTO.getSprachen().get(0).getName(), addStudentInformationDTO.getSprachen().get(0).getName());
        assertEquals(studentProfileDTO.getSprachen().get(0).getLevel(), addStudentInformationDTO.getSprachen().get(0).getLevel());

        // Assert Taetigkeitsfeld Information
        assertEquals(studentProfileDTO.getTaetigkeitsfelder().get(0).getName(), addStudentInformationDTO.getTaetigkeitsfelder().get(0).getName());

        // Assert Kenntnis Information
        assertEquals(studentProfileDTO.getKenntnisse().get(0).getName(), addStudentInformationDTO.getKenntnisse().get(0).getName());

    }

    @Test
    public void addStudentInformation() throws ProfileException, PersistenceException {

        User user = User.builder()
                .userid("SaschaAldaFan")
                .password("123456")
                .email("SaschaAldaFanNr1@AldaFans.de")
                .build();

        Student student1 = Student.builder()
                .vorname("Sina")
                .nachname("Schmidt")
                .build();

        student1.setUser(user);

        studentRepository.save(student1);

        Student student = studentRepository.findByUserID("SaschaAldaFan").get();
        System.out.println("Student:" + student.getUser().getUserid());

        changeStudentInformationDTO = ChangeStudentInformationDTO.builder()
                .geburtsdatum(LocalDate.of(2001, 1, 1))
                .studiengang("Wirtschaftsinformatik")
                .studienbeginn(LocalDate.of(2020, 1, 1))
                .matrikelnummer("9012305678")
                .lebenslauf("Ich bin der größe Sascha Alda Fan")
                .beschreibung("Ich bin ein toller Student.")
                .telefonnummer("0124123456789")
                .build();

        // To add
        KenntnisDTO kenntnisDTOAdd = KenntnisDTO.builder()
                .name("C++")
                .build();

        SpracheDTO spracheDTOAdd = SpracheDTO.builder()
                .name("Französisch")
                .level("B2")
                .build();

        QualifikationsDTO qualifikationDTOAdd = QualifikationsDTO.builder()
                .beschreibung("Ich habe ein Praktikum bei No Code absolviert. Es hat mir nicht gefallen Adalvia war defintiv besser. Spaß :)")
                .bereich("Software Entwicklung")
                .bezeichnung("SaaS Entwickler")
                .institution("No Code GmbH")
                .von(LocalDate.of(2020, 8, 1))
                .bis(LocalDate.of(2020, 8, 5))
                .beschaeftigungsart("Praktikum")
                .id(-1)
                .build();

        TaetigkeitsfeldDTO taetigkeitsfeldDTOAdd = TaetigkeitsfeldDTO.builder()
                .name("Software Design")
                .build();

        // Create Lists
        List<TaetigkeitsfeldDTO> addTaetigkeitsfelder = new ArrayList<>();
        List<SpracheDTO> addSprachen = new ArrayList<>();
        List<QualifikationsDTO> addQulifikationen = new ArrayList<>();
        List<KenntnisDTO> addKenntnisse = new ArrayList<>();


        // Add to Lists
        addTaetigkeitsfelder.add(taetigkeitsfeldDTOAdd);
        addSprachen.add(spracheDTOAdd);
        addQulifikationen.add(qualifikationDTOAdd);
        addKenntnisse.add(kenntnisDTOAdd);


        /*// Build DeletionStudentInformationDTO
        deletionStudentInformationDTO.setKenntnisse(null);
        deletionStudentInformationDTO.setQualifikationen(null);
        deletionStudentInformationDTO.setSprachen(null);
        deletionStudentInformationDTO.setTaetigkeitsfelder(null);*/

        // Build AddStudentInformationDTO
        addStudentInformationDTO.setKenntnisse(addKenntnisse);
        addStudentInformationDTO.setQualifikationen(addQulifikationen);
        addStudentInformationDTO.setSprachen(addSprachen);
        addStudentInformationDTO.setTaetigkeitsfelder(addTaetigkeitsfelder);

        // Build UpdateStudentInformationDTO
        UpdateStudentProfileDTO updateStudentProfileDTO = UpdateStudentProfileDTO.builder()
                .addStudentInformationDTO(addStudentInformationDTO)
                .changeStudentInformationDTO(changeStudentInformationDTO)
                .deletionStudentInformationDTO(deletionStudentInformationDTO)
                .build();

        StudentProfileDTO newstudentProfileDTO = StudentProfileDTO.builder()
                .email("sina.schmidt@aldavia-mail.de")
                .vorname("Sina")
                .nachname("Schmidt")
                .geburtsdatum(LocalDate.of(2001, 1, 1))
                .studiengang("Wirtschaftsinformatik")
                .studienbeginn(LocalDate.of(2020, 1, 1))
                .matrikelNummer("9012305678")
                .lebenslauf("Ich bin der größe Sascha Alda Fan")
                .beschreibung("Ich bin ein toller Student.")
                .telefonnummer("0124123456789")
                .kenntnisse(addKenntnisse)
                .sprachen(addSprachen)
                .qualifikationen(addQulifikationen)
                .taetigkeitsfelder(addTaetigkeitsfelder)
                .build();


        studentProfileControl.updateStudentProfile(newstudentProfileDTO, student1.getUser().getUserid());


        StudentProfileDTO studentProfileDTO = studentProfileControl.getStudentProfile(student1.getUser().getUserid());

        // Assert Student Information
        assertEquals(studentProfileDTO.getVorname(), student1.getVorname());
        assertEquals(studentProfileDTO.getNachname(), student1.getNachname());
        assertEquals(studentProfileDTO.getGeburtsdatum(), changeStudentInformationDTO.getGeburtsdatum());
        assertEquals(studentProfileDTO.getStudiengang(), changeStudentInformationDTO.getStudiengang());
        assertEquals(studentProfileDTO.getStudienbeginn(), changeStudentInformationDTO.getStudienbeginn(), "Studienbeginn ist nicht gleich");
        assertEquals(studentProfileDTO.getMatrikelNummer(), changeStudentInformationDTO.getMatrikelnummer());
        assertEquals(studentProfileDTO.getLebenslauf(), changeStudentInformationDTO.getLebenslauf());

        // Assert User Information
        assertEquals(studentProfileDTO.getBeschreibung(), changeStudentInformationDTO.getBeschreibung());
        assertEquals(studentProfileDTO.getTelefonnummer(), changeStudentInformationDTO.getTelefonnummer());

        // Assert Qualifikation Information
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschreibung(), addStudentInformationDTO.getQualifikationen().get(0).getBeschreibung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBereich(), addStudentInformationDTO.getQualifikationen().get(0).getBereich());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBezeichnung(), addStudentInformationDTO.getQualifikationen().get(0).getBezeichnung());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getInstitution(), addStudentInformationDTO.getQualifikationen().get(0).getInstitution());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getVon(), addStudentInformationDTO.getQualifikationen().get(0).getVon());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBis(), addStudentInformationDTO.getQualifikationen().get(0).getBis());
        assertEquals(studentProfileDTO.getQualifikationen().get(0).getBeschaeftigungsart(), addStudentInformationDTO.getQualifikationen().get(0).getBeschaeftigungsart());

        // Assert Sprache Information
        assertEquals(studentProfileDTO.getSprachen().get(0).getName(), addStudentInformationDTO.getSprachen().get(0).getName());
        assertEquals(studentProfileDTO.getSprachen().get(0).getLevel(), addStudentInformationDTO.getSprachen().get(0).getLevel());

        // Assert Taetigkeitsfeld Information
        assertEquals(studentProfileDTO.getTaetigkeitsfelder().get(0).getName(), addStudentInformationDTO.getTaetigkeitsfelder().get(0).getName());

        // Assert Kenntnis Information
        assertEquals(studentProfileDTO.getKenntnisse().get(0).getName(), addStudentInformationDTO.getKenntnisse().get(0).getName());


    }

}
