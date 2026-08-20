package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.BookingRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.BookingResponseDto;
import movieBookingapplication.Entity.Booking;
import movieBookingapplication.Enum.BookingStatus;
import movieBookingapplication.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingRequestDto dto){
        BookingResponseDto res=bookingService.addBooking(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);

    }

    @GetMapping("/getall")
    public ResponseEntity<List<BookingResponseDto>> getAllBookings(){
        List<BookingResponseDto> res=bookingService.getAllBookings();
        return ResponseEntity.ok(res);
    }


    @GetMapping("/userid/{id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingById(@PathVariable Long id){
        List<BookingResponseDto> res=bookingService.getBookingByUserId(id);
        return ResponseEntity.ok(res);
    }



    @GetMapping("/showId/{id}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByshowId(@PathVariable Long id){
        List<BookingResponseDto> res=bookingService.getbookingByShowId(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/confirm/{id}")
    public ResponseEntity<BookingResponseDto> confirmBooking(
            @PathVariable Long id,
            @RequestParam String paymentId,
            @RequestParam(defaultValue = "SUCCESS") String status) {
        BookingResponseDto res = bookingService.VerifyAndConfirmBooking(id, paymentId, status);
        return ResponseEntity.ok(res);
    }

//    @PutMapping("/confirm/{id}")
//    public ResponseEntity<BookingResponseDto> ConfirmBooking(@PathVariable Long id){
//        BookingResponseDto res=bookingService.confirmBookingById(id);
//        return ResponseEntity.ok(res);
//
//    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable Long id){
        BookingResponseDto res=bookingService.cancelBookingById(id);
        return ResponseEntity.ok(res);

    }


    @GetMapping("/getbystatus/{bookingStatus}")
    public ResponseEntity<List<BookingResponseDto>> getBookingByStatus(@PathVariable BookingStatus bookingStatus){
        List<BookingResponseDto> res=bookingService.getBookingByBookingStatus(bookingStatus);
        return ResponseEntity.ok(res);

    }

}
