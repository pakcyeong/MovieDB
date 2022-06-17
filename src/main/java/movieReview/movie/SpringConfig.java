package movieReview.movie;

import movieReview.movie.Service.MovieService;
import movieReview.movie.repository.JdbcMovieRepository;
import movieReview.movie.repository.MovieRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class SpringConfig {

    private final DataSource dataSource;

    public SpringConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MovieService movieService() {
        return new MovieService(movieRepository());
    }

    @Bean
    public MovieRepository movieRepository() {
        return new JdbcMovieRepository(dataSource);
    }
}