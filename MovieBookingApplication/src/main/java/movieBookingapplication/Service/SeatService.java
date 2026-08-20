package movieBookingapplication.Service;

import movieBookingapplication.Dtos.RequestDtos.SeatRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.SeatResponseDto;
import movieBookingapplication.Entity.Screen;
import movieBookingapplication.Entity.Seat;
import movieBookingapplication.ExceptionHandler.ScreenNotFoundException;
import movieBookingapplication.ExceptionHandler.SeatUnavailableException;
import movieBookingapplication.Repository.ScreenRepository;
import movieBookingapplication.Repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private ScreenRepository screenRepository;

    public SeatResponseDto mapToDto(Seat seat){
        SeatResponseDto res=new SeatResponseDto();
        res.setId(seat.getId());
        res.setSeatNumber(seat.getSeatNumber());
        res.setSeatType(seat.getSeatType());
        res.setPrice(seat.getPrice());
        res.setScreenId(seat.getScreen().getId());
        res.setScreenName(seat.getScreen().getScreenName());
        return res;

    }


    //add setas here
    public SeatResponseDto addSeat(SeatRequestDto dto) {

        Screen screen = screenRepository.findById(dto.getScreenId())
                .orElseThrow(() -> new ScreenNotFoundException("Screen Not Found with ID: "
                        + dto.getScreenId()));

        Seat seat = new Seat();
        seat.setSeatNumber(dto.getSeatNumber());
        seat.setSeatType(dto.getSeatType());
        seat.setPrice(dto.getPrice());
        seat.setScreen(screen);

        Seat savedSeat=seatRepository.save(seat);
        return mapToDto(savedSeat);
    }

    //get all seats
    public List<SeatResponseDto> getSeatsByScreenId(Long screenId) {
        List<Seat> seats=seatRepository.findByScreenId(screenId);

        List<SeatResponseDto> list=new ArrayList<>();
        for(Seat seat : seats){
            SeatResponseDto res=mapToDto(seat);
            list.add(res);
        }
        return list;
    }

    //here fetch all seats detals
    public List<SeatResponseDto> getAllSeats(){
        List<Seat> list=seatRepository.findAll();

        List<SeatResponseDto> res=new ArrayList<>();
        for(Seat seat : list){
            SeatResponseDto dto=mapToDto(seat);
            res.add(dto);
        }
        return res;

    }

    //get Seat by seat Id
    public SeatResponseDto getById(Long id){

        Seat seat=seatRepository.findById(id).orElseThrow(()->
                new SeatUnavailableException("Seat Not Found.."));

        SeatResponseDto res=mapToDto(seat);
        return res;
    }

    //delete seat bt seat id
    public void deleteSeatById(Long id) {
        Seat res=seatRepository.findById(id).orElseThrow(()->
                new SeatUnavailableException("Seat Not Found."));
        seatRepository.delete(res);
    }
}