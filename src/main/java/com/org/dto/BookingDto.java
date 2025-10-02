package com.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private String bookingNumber;
    private LocalDateTime bookingTime;
    private UserDto userDto;
    private ShowDto showDto;
    private String status;
    private Double totalAmount;
    private List<ShowSeatDto> showSeatDtos;
    private PaymentDto paymentDto;

}
