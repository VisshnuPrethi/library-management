package com.library.repository;
import com.library.model.BorrowRequest;
import com.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {
    @Query("SELECT br FROM BorrowRequest br JOIN FETCH br.user JOIN FETCH br.book WHERE br.user=:user ORDER BY br.requestedAt DESC")
    List<BorrowRequest> findByUser(@Param("user") User user);
    @Query("SELECT br FROM BorrowRequest br WHERE br.user.id=:uid AND br.book.id=:bid AND br.status='PENDING'")
    List<BorrowRequest> findPending(@Param("uid") Long uid, @Param("bid") Long bid);
    @Query("SELECT br FROM BorrowRequest br JOIN FETCH br.user JOIN FETCH br.book ORDER BY br.requestedAt DESC")
    List<BorrowRequest> findAllWithDetails();
}
