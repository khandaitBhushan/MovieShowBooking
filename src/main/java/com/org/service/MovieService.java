package com.org.service;

import com.org.Models.Movie;
import com.org.dto.MovieDto;
import com.org.exception.ResourceNotFoundException;
import com.org.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public MovieDto createMovie(MovieDto movieDto)
    {
        Movie movie=mapToEntity(movieDto);
        Movie saveMovie=movieRepository.save(movie);
        return mapToDto(saveMovie);
    }


    public MovieDto getMovieById(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie not found with id : "+id));
        return mapToDto(movie);
    }

    public List<MovieDto> getAllMovies(){
        List<Movie>movies = movieRepository.findAll();
        return movies.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByLanguage(String language){
        return movieRepository.findByLanguage(language)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }


    public List<MovieDto> getMoviesByGenre(String genre){
        return movieRepository.findByGenre(genre)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MovieDto> getMoviesByTitle(String title){
        return movieRepository.findByTitleContaining(title)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MovieDto updateMovie(Long id,MovieDto movieDto){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No Movie found with id : "+id));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMin(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return mapToDto(movieRepository.save(movie));
    }

    public void deleteMovie(Long id){
        Movie movie = movieRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Movie does not exist with id : "+id));
        movieRepository.deleteById(id);
    }

    private MovieDto mapToDto(Movie movie)
    {
        MovieDto movieDto=new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDuration(movie.getDurationMin());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }

    public Movie mapToEntity(MovieDto movieDto)
    {
        Movie movie=new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMin(movieDto.getDuration());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;
    }

}
