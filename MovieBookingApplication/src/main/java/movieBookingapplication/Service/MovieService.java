package movieBookingapplication.Service;

import movieBookingapplication.Dtos.RequestDtos.MovieRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.MovieResponseDto;
import movieBookingapplication.Entity.Movie;
import movieBookingapplication.ExceptionHandler.MovieNotFoundException;
import movieBookingapplication.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;


    public MovieResponseDto mapToDto(Movie movie){

        MovieResponseDto res=new MovieResponseDto();
        res.setId(movie.getId());
        res.setTitle(movie.getTitle());
        res.setDescription(movie.getDescription());
        res.setLanguage(movie.getLanguage());
        res.setGenre(movie.getGenre());
        res.setDurationMins(movie.getDurationMins());
        res.setReleaseDate(movie.getReleaseDate());
        res.setPosterUrl(movie.getPosterUrl());
        return res;
    }


    public MovieResponseDto addMovie(MovieRequestDto movieRequestDto) {
        Movie movie=new Movie();

        movie.setTitle(movieRequestDto.getTitle());
        movie.setDescription(movieRequestDto.getDescription());
        movie.setGenre(movieRequestDto.getGenre());
        movie.setLanguage(movieRequestDto.getLanguage());
        movie.setDurationMins(movieRequestDto.getDurationMins());
        movie.setReleaseDate(movieRequestDto.getReleaseDate());
        movie.setPosterUrl(movieRequestDto.getPosterUrl());

        Movie savedMovie=movieRepository.save(movie);
        return mapToDto(savedMovie);
    }


    public List<MovieResponseDto> getAllmovies() {
        List<Movie> movies=movieRepository.findAll();

        List<MovieResponseDto> list=new ArrayList<>();
        for( Movie movie : movies){
            MovieResponseDto res=mapToDto(movie);
            list.add(res);
        }
        return list;
    }



    public List<MovieResponseDto> getByGenre(String genre) {
        List<Movie> list = movieRepository.
                findByGenreContainingIgnoreCase(genre);

        if (list.isEmpty()) {
            throw new MovieNotFoundException("No Movie Found By Genre : " + genre);
        }

        List<MovieResponseDto> responseList = new ArrayList<>();
        for (Movie movie : list) {
            MovieResponseDto dto = mapToDto(movie);
            responseList.add(dto);
        }
        return responseList;
    }



    public List<MovieResponseDto> getMovieByMovieTitle(String title) {
        List<Movie> list = movieRepository.
                findByTitleContainingIgnoreCase(title);

        if (list.isEmpty()) {
            throw new MovieNotFoundException("No Movie Found By Title : " + title);
        }
        List<MovieResponseDto> responseList = new ArrayList<>();
        for (Movie movie : list) {
            MovieResponseDto dto = mapToDto(movie);
            responseList.add(dto);
        }
        return responseList;
    }


    public List<MovieResponseDto> getmovieBylanguage(String language) {
        List<Movie> list = movieRepository.
                findByLanguageContainingIgnoreCase(language);

        if (list.isEmpty()) {
            throw new MovieNotFoundException("No Movie Found By Language : " + language);
        }
        List<MovieResponseDto> responseList = new ArrayList<>();
        for (Movie movie : list) {
            MovieResponseDto dto = mapToDto(movie);
            responseList.add(dto);
        }
        return responseList;


    }


    public MovieResponseDto updatemovies(Long id, MovieRequestDto movieDto) {

        Movie movie=movieRepository.findById(id).orElseThrow(
                ()->new MovieNotFoundException("Movie Not Found with : "+id));

        movie.setTitle(movieDto.getTitle());
        movie.setGenre(movieDto.getGenre());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());

        Movie savedmovie=movieRepository.save(movie);
        return mapToDto(savedmovie);
    }


    public void deleteById(Long id) {
        Movie movie=movieRepository.findById(id).orElseThrow(
                ()->new MovieNotFoundException("Movie Not Found with"+id));
        movieRepository.deleteById(id);

    }


}
