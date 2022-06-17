package movieReview.movie.repository;

import movieReview.movie.controller.MovieForm;
import movieReview.movie.domain.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Movie save(Movie movie);
    Optional<Movie> findbyName(String name);
    List<Movie> findCond(MovieForm form);
    List<Movie> findAll();
}
