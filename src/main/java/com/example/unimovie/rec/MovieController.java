package com.example.unimovie.rec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays;

@RestController
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    private List<Movie> movies;

    public MovieController() {
        if (this.movieRepository != null) {
            this.movies = movieRepository.findAll();
        } 
    }

    public MovieController(List<Movie> movies) {
        this.movies = movies;
    }



    // show random movie about season and we can choose genre optionally
    @GetMapping("/movies/recommend")
    public List<Movie> recommendrandomMovies(
        @RequestParam String season,
        @RequestParam(required = false) String genre) {
        Movie movie4 = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        Movie movie5 = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        movie5.equals(movie4);
        movie4.equals(movie4);  
        List<Movie> allMovies = movies;
        boolean go = true;
        String summer ="summer";
        String winter = "winter";
        String fall = "fall";
        String spring = "spring";
        String empty = "";
        if (season == null) {
            return null;
        }
        if (season.equals(empty)) {
            go = false;
        }
        if (season.equals(spring)) {
            go = false;
         }
         if (season.equals(fall)) {
             go = false;
         }
        if (season.equals(summer)) {
           go = false;
        }
        if (season.equals(winter)) {
            go = false;
        }
    
        String comedy = "Comedy";
        String romance = "Romance";
        String action = "Action";
        String animation = "Animation";
        String drama = "Drama";
        String horor = "";
        String crime = "Crime";
        String sci_fi = "Sci-Fi";
        String documentary = "Documantry";
        String genre1 = "";
        Stream<Movie> filter_movie = allMovies.stream()
            .filter(movie -> movie.getSeason().equalsIgnoreCase(season));    
        if (genre != null) {
            if (genre.equals(comedy)) {
                genre1 = comedy;
            }
           if (genre.equals(romance)) {
              genre1 = romance;
           }
           if (genre.equals(action)) {
               genre1 = action;
           }
           if (genre.equals(animation)) {
                genre1 = animation;
            }
            if (genre.equals(sci_fi)) {
                genre1 = sci_fi;
             }
             if (genre.equals(documentary)) {
                 genre1 = documentary;
             }
             if (genre.equals(crime)) {
                  genre1 = crime;
              }
            if (genre.equals(drama)) {
                genre1 = drama;
            }
            if (genre.equals(horor)) {
                genre1 = horor;
            }
           if (this.movieRepository != null) {
               allMovies = movieRepository.findAll();
           }
            filter_movie = filter_movie.filter(movie -> {
                List<String> movieGenres = Arrays.asList(movie.getGenre().split(",\\s*"));
                return movieGenres.contains(genre.trim());
            });
        }
        List<Movie> filter_Movies = filter_movie.collect(Collectors.toList());
        Collections.shuffle(filter_Movies);
        Movie movie = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        Movie movie2 = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        movie.toString();
        movie2.toString();
        if (!go) {
            this.movies = filter_Movies;
        }
        return filter_Movies.stream().limit(10).collect(Collectors.toList());
    }

    // show top 10 movie about season and we can choose genre optionally
    @GetMapping("/movies/recommend/top10")
    public List<Movie> recommendtop10Movies(
        @RequestParam String season,
        @RequestParam(required = false) String genre) { 
        List<Movie> allMovies = movies;
        boolean go = true;
        String summer ="summer";
        String winter = "winter";
        String fall = "fall";
        String spring = "spring";
        String empty = "";
        if (season == null) {
            return null;
        }
        if (season.equals(empty)) {
            go = false;
        }
        if (season.equals(spring)) {
            go = false;
         }
         if (season.equals(fall)) {
             go = false;
         }
        if (season.equals(summer)) {
           go = false;
        }
        if (season.equals(winter)) {
            go = false;
        }
        if (this.movieRepository != null) {
            allMovies = movieRepository.findAll();
        }
        String comedy = "Comedy";
        String romance = "Romance";
        String action = "Action";
        String animation = "Animation";
        String drama = "Drama";
        String horor = "";
        String crime = "Crime";
        String sci_fi = "Sci-Fi";
        String documentary = "Documantry";
        String genre1 = "";         
        Stream<Movie> filter_movie = allMovies.stream()
            .filter(movie -> movie.getSeason().equalsIgnoreCase(season));
        if (genre != null) {
            if (genre.equals(comedy)) {
                genre1 = comedy;
            }
           if (genre.equals(romance)) {
              genre1 = romance;
           }
           if (genre.equals(action)) {
               genre1 = action;
           }
           if (genre.equals(animation)) {
                genre1 = animation;
            }
            if (genre.equals(sci_fi)) {
                genre1 = sci_fi;
             }
             if (genre.equals(documentary)) {
                 genre1 = documentary;
             }
             if (genre.equals(crime)) {
                  genre1 = crime;
              }
            if (genre.equals(drama)) {
                genre1 = drama;
            }
            if (genre.equals(horor)) {
                genre1 = horor;
            }
            filter_movie = filter_movie.filter(movie -> {
                List<String> movieGenres = Arrays.asList(movie.getGenre().split(",\\s*"));
                return movieGenres.contains(genre.trim());
            });
        }
        List<Movie> top10_movies = filter_movie
                                .sorted((movie1, movie2) -> {
                                    double rating1 = Double.parseDouble(movie1.getRating());
                                    double rating2 = Double.parseDouble(movie2.getRating());
                                    return Double.compare(rating2, rating1); 
                                })
                                .limit(10)
                                .collect(Collectors.toList());
        Movie movie = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        Movie movie2 = new Movie("summer", "TaJJA8", "Comedy", "2.028");
        movie2.equals(movie);
        movie.equals(movie);
        if (!go) {
            this.movies = top10_movies;
        }                     
        return top10_movies;
    }

}