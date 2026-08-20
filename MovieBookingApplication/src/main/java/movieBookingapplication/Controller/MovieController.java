package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.MovieRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.MovieResponseDto;
import movieBookingapplication.Entity.Movie;
import movieBookingapplication.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;


    @PostMapping("/create")
    public ResponseEntity<MovieResponseDto> addMovie(@RequestBody
                                          MovieRequestDto movieRequestDto){
        MovieResponseDto movie=movieService.addMovie(movieRequestDto);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(movie);

    }

    @GetMapping("/getall")
    public ResponseEntity<List<MovieResponseDto>>  getAllMovies(){
        List<MovieResponseDto> movies=movieService.getAllmovies();
        return ResponseEntity.ok(movies);
    }


    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieResponseDto>> getMovieByGenre(@PathVariable String genre){
        List<MovieResponseDto> res=movieService.getByGenre(genre);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<MovieResponseDto>> getMovieByTitle(@PathVariable String title){

        List<MovieResponseDto> res=movieService.getMovieByMovieTitle(title);
        return ResponseEntity.ok(res);

    }


    @GetMapping("/language/{language}")
    public ResponseEntity<List<MovieResponseDto>> getMovieBylanguage(@PathVariable String language){
       List<MovieResponseDto> res=movieService.getmovieBylanguage(language);
        return ResponseEntity.ok(res);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id,
                                             @RequestBody MovieRequestDto movieDto){

        MovieResponseDto res=movieService.updatemovies(id, movieDto);
        return ResponseEntity.ok(res);

    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id){
        movieService.deleteById(id);
        return ResponseEntity.ok("Movie Deleted Successfully..");
    }


}
