package com.library.dto;
import jakarta.validation.constraints.*;
public class BookDto {
    @NotBlank @Size(max=255) private String title;
    @NotBlank @Size(max=255) private String author;
    @NotBlank @Size(max=100) private String category;
    @Size(max=20) private String isbn;
    @NotNull @Min(0) @Max(9999) private Integer availableCopies;
    public String getTitle(){return title;} public void setTitle(String v){this.title=v;}
    public String getAuthor(){return author;} public void setAuthor(String v){this.author=v;}
    public String getCategory(){return category;} public void setCategory(String v){this.category=v;}
    public String getIsbn(){return isbn;} public void setIsbn(String v){this.isbn=v;}
    public Integer getAvailableCopies(){return availableCopies;}
    public void setAvailableCopies(Integer v){this.availableCopies=v;}
}
