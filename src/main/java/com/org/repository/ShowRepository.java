package com.org.repository;

import com.org.Models.Screen;
import com.org.Models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show,Long> {
    List<Show> findByMovieId(Long movieID);
    List<Show> findByScreenId(Long screenID);
    List<Show> findByStartTimeBetween(LocalDateTime start,LocalDateTime end);
    List<Show> findByMovie_IdAndScreen_Theater_City(Long movieID, String city);

}
