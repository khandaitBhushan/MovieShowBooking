package com.org.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TheaterDto {
    private Long id;
    private String address;
    private String city;
    private String name;
    private Integer totalScreens;
}
