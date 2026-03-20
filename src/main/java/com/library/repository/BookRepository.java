package com.library.repository;
import com.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',:k,'%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%',:k,'%')) OR LOWER(b.category) LIKE LOWER(CONCAT('%',:k,'%'))")
    List<Book> search(@Param("k") String keyword);
}
