package movieReview.movie.Service;

import movieReview.movie.controller.MovieForm;
import movieReview.movie.domain.Movie;
import movieReview.movie.repository.JdbcMovieRepository;
import movieReview.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Long join(Movie movie) {
        validateDuplicateMovie(movie);
        movieRepository.save(movie);
        return movie.getNid();
    }

    private void validateDuplicateMovie(Movie movie) {
        movieRepository.findbyName(movie.getName())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 영화입니다.");
                });
    }

    public List<Movie> findCondMovies(MovieForm form) {
        return movieRepository.findCond(form);
    }

    public List<Movie> findMovies() {
        return movieRepository.findAll();
    }

}
