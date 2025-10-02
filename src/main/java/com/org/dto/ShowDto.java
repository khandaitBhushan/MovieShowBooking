package com.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowDto {
    private Long Id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private MovieDto movieDto;
    private ScreenDto screenDto;
    private List<ShowSeatDto> availableSeats;
}
