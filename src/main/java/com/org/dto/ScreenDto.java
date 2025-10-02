package com.org.dto;

import com.org.Models.Theater;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenDto {
    private Long id;
    private String name;
    private Integer totalSeat;
    private TheaterDto theaterDto;
}
