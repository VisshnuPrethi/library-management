package com.library.config;

import com.library.model.*;
import com.library.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository users;
    private final BookRepository books;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository users, BookRepository books, PasswordEncoder encoder) {
        this.users = users; this.books = books; this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!users.existsByUsername("admin")) {
            users.save(new User("admin","admin@library.com", encoder.encode("Admin1234!"), Role.LIBRARIAN));
            log.info("Created admin account");
        }
        if (!users.existsByUsername("user1")) {
            users.save(new User("user1","user1@library.com", encoder.encode("User1234!"), Role.USER));
            log.info("Created user1 account");
        }
        if (books.count() == 0) {
            books.save(new Book("Clean Code","Robert C. Martin","Programming","978-0132350884",3));
            books.save(new Book("The Pragmatic Programmer","David Thomas","Programming","978-0135957059",2));
            books.save(new Book("Design Patterns","Gang of Four","Programming","978-0201633610",1));
            books.save(new Book("The Great Gatsby","F. Scott Fitzgerald","Fiction","978-0743273565",4));
            books.save(new Book("1984","George Orwell","Fiction","978-0451524935",2));
            books.save(new Book("Sapiens","Yuval Noah Harari","History","978-0062316097",3));
            books.save(new Book("A Brief History of Time","Stephen Hawking","Science","978-0553380163",2));
            books.save(new Book("Introduction to Algorithms","CLRS","Computer Science","978-0262046305",1));
            log.info("Seeded 8 books");
        }
    }
}
