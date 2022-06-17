package movieReview.movie.controller;


import movieReview.movie.Service.MovieService;
import movieReview.movie.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService){
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String home() {
        return "main";
    }

    @GetMapping("/movies/create")
    public String createForm(){
        return "movies/create";
    }

    @PostMapping(value = "/movies/create")
    public String create(MovieForm form){
        Movie movie = new Movie();
        movie.setName(form.getName());
        movie.setDname(form.getDname());
        movie.setActor(form.getActor());
        movie.setRdate(form.getRdate());
        movie.setGenre(form.getGenre());

        movieService.join(movie);

        return "redirect:/movies";
    }

    @GetMapping(value = "/movies")
    public String mList(Model model){
        List<Movie> movies = movieService.findMovies();
        model.addAttribute("movies", movies);
        return "movies";
    }

    @GetMapping("/movies/search")
    public String searchForm() {
        return "movies/search";
    }

    @PostMapping(value = "/movies/search")
    public String mSearch(MovieForm form, Model model){
        List<Movie> movies = movieService.findCondMovies(form);
        model.addAttribute("movies", movies);
        return "movies/list";
    }

}
