package com.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowSeatDto {
    private Long id;
    private SeatDto seatDto;
    private String status;
    private Double price;

}
