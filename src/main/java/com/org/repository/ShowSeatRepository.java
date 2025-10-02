package com.org.repository;

import com.org.Models.Show;
import com.org.Models.ShowSeats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeats,Long> {
    List<ShowSeats> findByShowId(Long showID);
    List<ShowSeats> findByShowIdAndStatus(Long showID, String status);

}
