package com.example.restservice;
import com.example.restservice.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
//        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//        Object dataSource = context.getBean("dataSource");
//        System.out.println(dataSource);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(AccountRepository repository, String username, String password) {
//        return args -> {
//            //repository.save(new Account(username, password));
//        };
//    }
}
