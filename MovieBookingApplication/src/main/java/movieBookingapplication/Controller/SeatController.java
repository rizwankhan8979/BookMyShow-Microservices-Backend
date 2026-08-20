package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.SeatRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.SeatResponseDto;
import movieBookingapplication.Service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    //add seat here
    @PostMapping("/create")
    public ResponseEntity<SeatResponseDto> createSeat(@RequestBody SeatRequestDto dto) {
        SeatResponseDto res = seatService.addSeat(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }


    @GetMapping("/screen/{screenId}")
    public ResponseEntity<List<SeatResponseDto>> getSeatsByScreenId(@PathVariable Long screenId) {
        List<SeatResponseDto> res = seatService.getSeatsByScreenId(screenId);
        return ResponseEntity.ok(res);
    }


    @GetMapping("/getall")
    public ResponseEntity<List<SeatResponseDto>> getAllSeats(){
        List<SeatResponseDto> res=seatService.getAllSeats();
        return ResponseEntity.ok(res);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<SeatResponseDto> getById(@PathVariable Long id){
        SeatResponseDto seat=seatService.getById(id);
        return ResponseEntity.ok(seat);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        seatService.deleteSeatById(id);
        return ResponseEntity.ok("Seat Delete Successfully.");
    }


}