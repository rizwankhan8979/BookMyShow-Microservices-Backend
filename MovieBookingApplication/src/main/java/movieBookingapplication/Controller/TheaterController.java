package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.TheaterRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.TheaterResponseDto;
import movieBookingapplication.Service.TheaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theater")
public class TheaterController {

    @Autowired
    private TheaterService theaterService;

    @PostMapping("/create")
    public ResponseEntity<TheaterResponseDto> createTheater(@RequestBody TheaterRequestDto dto){
        TheaterResponseDto res=theaterService.addTheater(dto);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(res);
    }

    @GetMapping("/get/{location}")
    public ResponseEntity<List<TheaterResponseDto>> getTheaterByLocation(@PathVariable String location){
        List<TheaterResponseDto> res=theaterService.getTheaterBylocation(location);
        return ResponseEntity.ok(res);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<TheaterResponseDto> updateTheater(@PathVariable  Long id, @RequestBody TheaterRequestDto dto){

        TheaterResponseDto res=theaterService.updateTheater(id, dto);
        return ResponseEntity.ok(res);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTheater(@PathVariable Long id){
        theaterService.deleteById(id);
        return ResponseEntity.ok("Theater Delete Succesfull..");

    }

}
