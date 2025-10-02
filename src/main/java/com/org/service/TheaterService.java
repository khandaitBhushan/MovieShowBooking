package com.org.service;

import com.org.Models.Theater;
import com.org.dto.TheaterDto;
import com.org.exception.ResourceNotFoundException;
import com.org.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;

    public TheaterDto createTheater(TheaterDto theaterDto){
        Theater theater = mapToEntity(theaterDto);
        return mapToDto(theaterRepository.save(theater));
    }

    public TheaterDto getTheaterById(Long id)
    {
        Theater theater=theaterRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Theater not found with id: "+id));
        return mapToDto(theater);
    }

    public List<TheaterDto> getAllTheaters(){
        List<Theater> theaterDtos = theaterRepository.findAll();
        return theaterDtos.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private List<TheaterDto> getAllTheaterByCity(String city)
    {
        List<Theater> theaters=theaterRepository.findByCity(city);
        return theaters.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    private TheaterDto mapToDto(Theater theater) {
        TheaterDto theaterDto=new TheaterDto();
        theaterDto.setId(theater.getId());
        theaterDto.setName(theater.getName());
        theaterDto.setCity(theater.getCity());
        theaterDto.setAddress(theater.getAddress());
        theaterDto.setTotalScreens(theater.getTotalScreen());
        return theaterDto;
    }

    private Theater mapToEntity(TheaterDto theaterDto) {
        Theater theater=new Theater();
        theater.setName(theaterDto.getName());
        theater.setAddress(theaterDto.getAddress());
        theater.setCity(theaterDto.getCity());
        theater.setTotalScreen(theaterDto.getTotalScreens());
        return theater;
    }
}
