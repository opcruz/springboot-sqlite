package com.demo.sqlite;

import com.demo.sqlite.models.Client;
import com.demo.sqlite.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SQLiteDemoApplication {

    private final ClientRepository clientRepository;

    public SQLiteDemoApplication(@Autowired ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SQLiteDemoApplication.class, args);
    }

    //    @Bean
    CommandLineRunner runner() {
        return args -> {
            Client saved = clientRepository.save(
                    Client.builder()
                            .name("Javier")
                            .surnames("Pérez")
                            .direction("Luna 5")
                            .province("Centro")
                            .phone("525353535")
                            .email("email@gmail.com")
                            .cp("52000")
                            .passwordhash("2689367b205c16ce32ed4200942b8b8b1e262dfc70d9bc9fbc77c49699a4f1df") // ok
                            .build()
            );
            System.out.println("Created client: " + saved);
        };
    }

}
