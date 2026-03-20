package com.library.dto;
import com.library.model.BorrowRequest;
import java.time.LocalDateTime;
public class BorrowRequestDto {
    private Long id;
    private String username, bookTitle, status;
    private Long bookId;
    private LocalDateTime requestedAt, processedAt;
    public BorrowRequestDto(BorrowRequest br) {
        this.id = br.getId();
        this.username = br.getUser().getUsername();
        this.bookId = br.getBook().getId();
        this.bookTitle = br.getBook().getTitle();
        this.status = br.getStatus().name();
        this.requestedAt = br.getRequestedAt();
        this.processedAt = br.getProcessedAt();
    }
    public Long getId(){return id;}
    public String getUsername(){return username;}
    public Long getBookId(){return bookId;}
    public String getBookTitle(){return bookTitle;}
    public String getStatus(){return status;}
    public LocalDateTime getRequestedAt(){return requestedAt;}
    public LocalDateTime getProcessedAt(){return processedAt;}
}
