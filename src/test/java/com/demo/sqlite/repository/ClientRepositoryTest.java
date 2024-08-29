package com.demo.sqlite.repository;

import com.demo.sqlite.model.entity.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = TestConfig.class)
@ActiveProfiles("test")
public class ClientRepositoryTest {

   @Autowired
   private ClientRepository clientRepository;

   private static final String TEST_EMAIL = "john@example.com";
   private static final String TEST_PASSWORD_HASH = "2689367B205C16CE32ED4200942B8B8B1E262DFC70D9BC9FBC77C49699A4F1DF";
   private static final String INVALID_EMAIL = "invalid_email@com";

   private static final String INVALID_PASSWORD_HASH = "invalid_password_hash";

   @Test
   public void testFindExistingClientByEmailAndPassword() {
      Optional<Client> clientFounded = clientRepository.findClientByEmailAndPassword(TEST_EMAIL, TEST_PASSWORD_HASH);
      assertTrue(clientFounded.isPresent());
      assertEquals(clientFounded.get().getEmail(), TEST_EMAIL);
      assertEquals(clientFounded.get().getPasswordHash(), TEST_PASSWORD_HASH);
   }

   @Test
   public void testFindNonExistingClientByEmailAndPassword() {
      Optional<Client> clientFounded = clientRepository.findClientByEmailAndPassword(INVALID_EMAIL,
            INVALID_PASSWORD_HASH);
      assertTrue(clientFounded.isEmpty());
   }

   @Test
   public void testIfExistsEmailWhenEmailExists() {
      Optional<Integer> optionalID = clientRepository.existEmail(TEST_EMAIL);
      assertTrue(optionalID.isPresent());
      assertEquals(optionalID.get(), 1);
   }

   @Test
   public void testIfExistsEmailWhenEmailDoesNotExists() {
      Optional<Integer> optionalID = clientRepository.existEmail(INVALID_EMAIL);
      assertTrue(optionalID.isEmpty());
   }

}