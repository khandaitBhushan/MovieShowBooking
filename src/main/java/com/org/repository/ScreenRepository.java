package com.org.repository;

import com.org.Models.Booking;
import com.org.Models.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScreenRepository extends JpaRepository<Screen,Long> {
    List<Screen> findByTheaterId(Long theaterID);

}
