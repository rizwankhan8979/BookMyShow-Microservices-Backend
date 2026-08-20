package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.ResponseDtos.ShowSeatResponse;
import movieBookingapplication.Service.ShowSeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/show-seat")
public class ShowSeatController {

    @Autowired
    private ShowSeatService showSeatService;

    //get all  showseats bye show id
    @GetMapping("/show/{showId}")
    public ResponseEntity<List<ShowSeatResponse>> getShowSeatsByShowId(@PathVariable Long showId) {
        List<ShowSeatResponse> res = showSeatService.getShowSeatsByShowId(showId);
        return ResponseEntity.ok(res);
    }

    //locked seat for 10 minutes
    @PutMapping("/lock")
    public ResponseEntity<List<ShowSeatResponse>> lockSeats(@RequestBody List<Long> showSeatIds) {
        List<ShowSeatResponse> res = showSeatService.lockSeats(showSeatIds);
        return ResponseEntity.ok(res);
    }
}
