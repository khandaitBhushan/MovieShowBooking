package com.org.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Movies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String language;
    private String genre;
    private Integer durationMin;
    private String posterUrl;
    private String releaseDate;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    List<Show>shows ;

}
