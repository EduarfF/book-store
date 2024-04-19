package org.example.springbootintro;

import java.math.BigDecimal;
import org.example.springbootintro.model.Book;
import org.example.springbootintro.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootIntroApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootIntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book harryPotter = new Book();
            harryPotter.setAuthor("Rowling");
            harryPotter.setIsbn("1234");
            harryPotter.setDescription("Interesting movie about a boy, who survived");
            harryPotter.setTitle("Harry Potter");
            harryPotter.setCoverImage("img");
            harryPotter.setPrice(BigDecimal.valueOf(99));

            bookService.save(harryPotter);
            System.out.println(bookService.findAll());
        };
    }
}
