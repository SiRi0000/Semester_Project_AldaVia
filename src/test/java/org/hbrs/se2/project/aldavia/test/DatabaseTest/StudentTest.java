package org.hbrs.se2.project.aldavia.test.DatabaseTest;

import org.hbrs.se2.project.aldavia.entities.Kenntnis;
import org.hbrs.se2.project.aldavia.entities.Student;
import org.hbrs.se2.project.aldavia.entities.User;
import org.hbrs.se2.project.aldavia.repository.StudentRepository;
import org.hbrs.se2.project.aldavia.repository.KenntnisseRepository;

import org.hbrs.se2.project.aldavia.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KenntnisseRepository kenntnisseRepository;

    private User user;
    private Student student;

    private List<Kenntnis> kenntnisse;

    //TODO: Implement tests for Student

    @BeforeAll
    public void setUp() {
        try {
            // Clean up
            studentRepository.deleteByMatrikelNummer("9042069");
            userRepository.deleteByUserid("Sascha");
            kenntnisseRepository.deleteById("Java_Test_Kenntnis");
            kenntnisseRepository.deleteById("BWL_Test_Kenntnis");
        }
        catch (Exception e) {
            System.out.println("Nothing to clean-up: " + e.getMessage());
        }

        //User

        user = new User();
        user.setUserid("Sascha");
        user.setPassword("123");
        user.setEmail("sascha@bonne.de");
        userRepository.save(user);

        //Kenntnisse

        kenntnisse = new ArrayList<>();

        Kenntnis kenntnis = new Kenntnis();
        kenntnis.setBezeichnung("Java_Test_Kenntnis");
        kenntnisseRepository.save(kenntnis);
        Kenntnis kenntnis2 = new Kenntnis();
        kenntnis2.setBezeichnung("BWL_Test_Kenntnis");
        kenntnisseRepository.save(kenntnis2);

        kenntnisse.add(kenntnis);
        kenntnisse.add(kenntnis2);

        //TODO Qualifikationen und Sprachen
    }

    @AfterEach
    public void cleanUp() {
        try{
            student = null;
        }
        catch (Exception e) {
            System.out.println("Error while cleaning up: " + e.getMessage());
        }
    }

    @BeforeEach
    public void init() {
        try{
            student = new Student();
            student.setVorname("Sascha");
            student.setNachname("Bonne");
            student.setStudienbeginn(LocalDate.of(2018, 10, 1));
            student.setStudiengang("Informatik");
            student.setGeburtsdatum(LocalDate.of(1998, 8, 10));
            student.setMatrikelNummer("9042069");
            student.setLebenslauf("Lebenslauf");
            student.setKenntnisse(kenntnisse);
            student.setUser(user);
        }
        catch (Exception e) {
            System.out.println("Error while initializing: " + e.getMessage());
        }
    }

    @AfterAll
    public void tearDown() {
        try{
            userRepository.delete(user);
            kenntnisseRepository.deleteById("Java_Test_Kenntnis");
            kenntnisseRepository.deleteById("BWL_Test_Kenntnis");
        }
        catch (Exception e) {
            System.out.println("Error while tearing down: " + e.getMessage());
        }
    }

   @Test
   public void testReadData(){
        studentRepository.save(student);
        Optional<Student> awaitStudent = studentRepository.findById(student.getStudentId());
        assertTrue(awaitStudent.isPresent());
        Student awaitStudent2 = awaitStudent.get();
        assertEquals(awaitStudent2.getVorname(), student.getVorname());
        assertEquals(awaitStudent2.getNachname(), student.getNachname());
        assertEquals(awaitStudent2.getStudienbeginn(), student.getStudienbeginn());
        assertEquals(awaitStudent2.getStudiengang(), student.getStudiengang());
        assertEquals(awaitStudent2.getGeburtsdatum(), student.getGeburtsdatum());
        assertEquals(awaitStudent2.getMatrikelNummer(), student.getMatrikelNummer());
        assertEquals(awaitStudent2.getLebenslauf(), student.getLebenslauf());
        assertEquals(awaitStudent2.getKenntnisse(), student.getKenntnisse());
        assertEquals(awaitStudent2.getUser(), student.getUser());
       studentRepository.delete(student);
   }
   @Test
   public void testFindByUser(){
       studentRepository.save(student);
       Optional<Student> awaitStudent = studentRepository.findByUser(user);
       assertTrue(awaitStudent.isPresent());
       Student awaitStudent2 = awaitStudent.get();
        assertEquals(awaitStudent2.getVorname(), student.getVorname());
        assertEquals(awaitStudent2.getNachname(), student.getNachname());
        assertEquals(awaitStudent2.getStudienbeginn(), student.getStudienbeginn());
        assertEquals(awaitStudent2.getStudiengang(), student.getStudiengang());
        assertEquals(awaitStudent2.getGeburtsdatum(), student.getGeburtsdatum());
        assertEquals(awaitStudent2.getMatrikelNummer(), student.getMatrikelNummer());
        assertEquals(awaitStudent2.getLebenslauf(), student.getLebenslauf());
        assertEquals(awaitStudent2.getKenntnisse(), student.getKenntnisse());
        assertEquals(awaitStudent2.getUser(), student.getUser());
        studentRepository.delete(student);
    }

    @Test
    public void testAddSameStudent(){
        studentRepository.save(student);
        assertThrows(Exception.class, () -> {
            studentRepository.save(student);
        });
        Student student2 = new Student();
        student2.setVorname("Mareike");
        student2.setNachname("Blohm");
        student2.setStudienbeginn(LocalDate.of(2018, 10, 1));
        student2.setStudienbeginn(LocalDate.of(2018, 10, 1));
        student2.setStudiengang("Wirtschaftsinformatik");
        student2.setGeburtsdatum(LocalDate.of(1998, 8, 10));
        student2.setMatrikelNummer("9042369");
        student2.setLebenslauf("Lebenslauf");
        student2.setKenntnisse(kenntnisse);
        assertThrows(Exception.class, () -> {
            student2.setUser(user);
        });
        studentRepository.delete(student);
    }
}
