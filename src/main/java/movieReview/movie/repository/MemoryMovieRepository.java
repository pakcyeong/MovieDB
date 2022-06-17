package movieReview.movie.repository;

import movieReview.movie.controller.MovieForm;
import movieReview.movie.domain.Movie;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMovieRepository implements MovieRepository {

    private static Map<Long, Movie> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Movie save(Movie movie) {
        movie.setNid(++sequence);
        store.put(movie.getNid(), movie);
        return movie;
    }

    @Override
    public Optional<Movie> findbyName(String name) {
        return store.values().stream()
                .filter(movie -> movie.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Movie> findCond(MovieForm form) {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(store.values());
    }
}
