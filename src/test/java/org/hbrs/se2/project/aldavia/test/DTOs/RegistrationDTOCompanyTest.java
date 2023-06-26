package org.hbrs.se2.project.aldavia.test.DTOs;

import org.hbrs.se2.project.aldavia.dtos.RegistrationDTOCompany;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RegistrationDTOCompanyTest {
    @Test
    public void testGettersAndSetters() {
        // Create an instance of RegistrationDTOCompany
        RegistrationDTOCompany company = new RegistrationDTOCompany();

        // Set values using setters
        company.setUserName("companyA");
        company.setCompanyName("Example Company");
        company.setMail("info@example.com");
        company.setPassword("password123");
        company.setRegistrationDate(20230625);
        company.setWebseite("www.example.com");

        // Test getters
        Assertions.assertEquals("companyA", company.getUserName());
        Assertions.assertEquals("Example Company", company.getCompanyName());
        Assertions.assertEquals("info@example.com", company.getMail());
        Assertions.assertEquals("password123", company.getPassword());
        Assertions.assertEquals(20230625, company.getRegistrationDate());
        Assertions.assertEquals("www.example.com", company.getWebseite());
    }
}

