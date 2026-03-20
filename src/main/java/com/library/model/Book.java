package com.library.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false) private String author;
    @Column(nullable = false) private String category;
    private String isbn;
    @Column(nullable = false) private Integer availableCopies = 1;

    public Book() {}
    public Book(String title, String author, String category, String isbn, int copies) {
        this.title = title; this.author = author; this.category = category;
        this.isbn = isbn; this.availableCopies = copies;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String v) { this.title = v; }
    public String getAuthor() { return author; }
    public void setAuthor(String v) { this.author = v; }
    public String getCategory() { return category; }
    public void setCategory(String v) { this.category = v; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String v) { this.isbn = v; }
    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer v) { this.availableCopies = v; }
}
