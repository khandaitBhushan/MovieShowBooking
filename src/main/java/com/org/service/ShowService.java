package com.org.service;

import com.org.Models.Movie;
import com.org.Models.Screen;
import com.org.Models.Show;
import com.org.Models.ShowSeats;
import com.org.dto.*;
import com.org.exception.ResourceNotFoundException;
import com.org.repository.MovieRepository;
import com.org.repository.ScreenRepository;
import com.org.repository.ShowRepository;
import com.org.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShowService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ScreenRepository screenRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private ShowSeatRepository showSeatRepository;

    private ShowDto createShow(ShowDto showDto) {
        Movie movie = movieRepository.findById(showDto.getMovieDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Movie does not found..."));
        Screen screen = screenRepository.findById(showDto.getScreenDto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Screen Not found...."));
        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());

        Show savedShow = showRepository.save(show);
        List<ShowSeats> availableSeats =
                showSeatRepository.findByShowIdAndStatus(savedShow.getId(), "AVAILABLE");
        return mapToDto(savedShow, availableSeats);
    }

    public ShowDto getShowById(Long id){
        Show show = showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show does not exists"));
        List<ShowSeats> availableSeats =
                showSeatRepository.findByShowIdAndStatus(show.getId(), "AVAILABLE");
        return mapToDto(show, availableSeats);
    }


    public List<ShowDto> getAllShow(){
        List<Show> showList = showRepository.findAll();
        return showList.stream().map(show->{
            List<ShowSeats>showSeatsList = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
            return mapToDto(show,showSeatsList);
        }).collect(Collectors.toList());
    }


    public List<ShowDto> getShowByMovie(Long movieId){
        List<Show>shows = showRepository.findByMovieId(movieId);
        return shows.stream().map(show->{
            List<ShowSeats>showSeatsList = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
            return mapToDto(show,showSeatsList);
        }).collect(Collectors.toList());
    }


    public List<ShowDto> getShowByMovieAndCity(Long id,String city){
        List<Show>shows = showRepository.findByMovie_IdAndScreen_Theater_City(id,city);
        return shows.stream().map(show->{
            List<ShowSeats>showSeatsList = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
            return mapToDto(show,showSeatsList);
        }).collect(Collectors.toList());
    }

    public List<ShowDto> getShowByDateRange(LocalDateTime startDate,LocalDateTime endDate){
        List<Show>shows = showRepository.findByStartTimeBetween(startDate,endDate);
        return shows.stream().map(show->{
            List<ShowSeats>showSeatsList = showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
            return mapToDto(show,showSeatsList);
        }).collect(Collectors.toList());
    }








    private ShowDto mapToDto(Show show, List<ShowSeats> availableSeats) {
        ShowDto showDto = new ShowDto();
        showDto.setId(show.getId());
        showDto.setStartTime(show.getStartTime());
        showDto.setEndTime(show.getEndTime());

        showDto.setMovieDto(new MovieDto(
                show.getMovie().getId(),
                show.getMovie().getTitle(),
                show.getMovie().getDescription(),
                show.getMovie().getLanguage(),
                show.getMovie().getGenre(),
                show.getMovie().getDurationMin(),
                show.getMovie().getReleaseDate(),
                show.getMovie().getPosterUrl()
        ));

        TheaterDto theaterDto = new TheaterDto(
                show.getScreen().getTheater().getId(),
                show.getScreen().getTheater().getName(),
                show.getScreen().getTheater().getAddress(),
                show.getScreen().getTheater().getCity(),
                show.getScreen().getTheater().getTotalScreen()
        );

        showDto.setScreenDto(new ScreenDto(
                show.getScreen().getId(),
                show.getScreen().getName(),
                show.getScreen().getTotalSeats(),
                theaterDto
        ));

        List<ShowSeatDto> seatDtos = availableSeats.stream()
                .map(seat -> {
                    ShowSeatDto seatDto = new ShowSeatDto();
                    seatDto.setId(seat.getId());
                    seatDto.setStatus(seat.getStatus());
                    seatDto.setPrice(seat.getPrice());

                    SeatDto baseSeatDto = new SeatDto();
                    baseSeatDto.setId(seat.getSeat().getId());
                    baseSeatDto.setSeatNumber(seat.getSeat().getSeatNumber());
                    baseSeatDto.setSeatType(seat.getSeat().getSeatType());
                    baseSeatDto.setBasePrice(seat.getSeat().getBasePrice());
                    seatDto.setSeatDto(baseSeatDto);
                    return seatDto;
                })
                .collect(Collectors.toList());

        showDto.setAvailableSeats(seatDtos);
        return showDto;
    }
}
