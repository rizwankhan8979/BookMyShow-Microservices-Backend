package movieBookingapplication.Service;

import movieBookingapplication.Dtos.RequestDtos.ShowRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.ShowResponseDto;
import movieBookingapplication.Entity.*;
import movieBookingapplication.Enum.ShowSeatStatus;
import movieBookingapplication.ExceptionHandler.MovieNotFoundException;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.ExceptionHandler.ScreenNotFoundException;
import movieBookingapplication.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;



    public ShowResponseDto mapToDto(Show show){
        ShowResponseDto res=new ShowResponseDto();

        res.setId(show.getId());
        res.setShowTime(show.getShowTime());
        res.setPrice(show.getPrice());
        res.setMovieId(show.getMovie().getId());
        res.setMovieTitle(show.getMovie().getTitle());
        res.setScreenId(show.getScreen().getId());
        res.setScreenName(show.getScreen().getScreenName());
        res.setTheaterId(show.getScreen().getTheater().getId());
        res.setTheaterName(show.getScreen().getTheater().getTheatername());
        res.setTheaterAddress(show.getScreen().getTheater().getAddress());
        return res;
    }



    public ShowResponseDto addShow(ShowRequestDto showRequestDto) {

        Movie movie = movieRepository.findById(showRequestDto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie Not Found with ID: "
                        + showRequestDto.getMovieId()));

        Screen screen = screenRepository.findById(showRequestDto.getScreenId())
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found with ID: "
                        + showRequestDto.getScreenId()));

        Show show = new Show();

        show.setShowTime(showRequestDto.getShowTime());
        show.setPrice(showRequestDto.getPrice());
        show.setMovie(movie);
        show.setScreen(screen);

        Show savedShow = showRepository.save(show);

        // ⚡ AUTO-GENERATE SHOW SEATS FOR ALL PHYSICAL SEATS IN THIS SCREEN

        List<Seat> physicalSeats = seatRepository.findByScreenId(screen.getId());

        if (physicalSeats != null && !physicalSeats.isEmpty()) {

            List<ShowSeat> showSeats = new ArrayList<>();

            for (Seat seat : physicalSeats) {
                ShowSeat ss = new ShowSeat();
                ss.setShow(savedShow);
                ss.setSeat(seat);
                ss.setSeatNumber(seat.getSeatNumber());

                if (seat.getPrice() != null) {
                    ss.setPrice(seat.getPrice());
                } else {
                    ss.setPrice(savedShow.getPrice());
                }

                ss.setStatus(ShowSeatStatus.AVAILABLE);
                showSeats.add(ss);
            }
            showSeatRepository.saveAll(showSeats);
        }

        return mapToDto(savedShow);
    }


    public List<ShowResponseDto> getAllShows() {
       List<Show> list=showRepository.findAll();
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Shows Not Found");
        }

       List<ShowResponseDto> res=new ArrayList<>();
       for(Show show : list){
           ShowResponseDto res1=mapToDto(show);
           res.add(res1);
       }
       return res;
    }



    public List<ShowResponseDto> getShowByMovie(Long id) {

        Optional<List<Show>> list = showRepository.findByMovieId(id);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Show Not Found with Movie ID : " + id);
        }
        List<Show> shows = list.get();//if methode is optional type then access and check khere

        List<ShowResponseDto> responseList = new ArrayList<>();

        for (Show show : shows) {

            ShowResponseDto dto = mapToDto(show);

            responseList.add(dto);
        }

        return responseList;
    }


    public List<ShowResponseDto> getShowByTheater(Long id) {
        Optional<List<Show>> list = showRepository.findByScreenTheaterId(id);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Show Not Found with Theater ID : " + id);
        }
        List<Show> shows = list.get();//if methode is optional type then access and check khere


        List<ShowResponseDto> responseList = new ArrayList<>();

        for (Show show : shows) {

            ShowResponseDto dto = mapToDto(show);

            responseList.add(dto);
        }

        return responseList;
    }


    public ShowResponseDto updateShowById(Long id, ShowRequestDto dto) {

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new MovieNotFoundException("Movie Not Found with ID: "
                        + dto.getMovieId()));

        Screen screen = screenRepository.findById(dto.getScreenId())
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found with ID: "
                        + dto.getScreenId()));

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show Not Found with ID: " + id));

        show.setShowTime(dto.getShowTime());
        show.setPrice(dto.getPrice());
        show.setMovie(movie);
        show.setScreen(screen);

       Show savedShow=showRepository.save(show);
       return mapToDto(savedShow);
    }



    public void deleteById(Long id) {
        if(!showRepository.existsById(id)){
            throw new ResourceNotFoundException("Show Not Found with ID: " + id);
        }

        List<Booking> bookings = showRepository.findById(id).get().getBookings();
        if(bookings != null && !bookings.isEmpty()){
            throw new ResourceNotFoundException("Cannot Delete Show With Existing Bookings..");
        }
        showRepository.deleteById(id);
    }
}
