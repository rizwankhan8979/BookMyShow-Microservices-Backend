package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.ScreenRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.ScreenResponseDto;
import movieBookingapplication.Service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/screen")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @PostMapping("/create")
    public ResponseEntity<ScreenResponseDto> createScreen(@RequestBody ScreenRequestDto dto) {
        ScreenResponseDto res = screenService.addScreen(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    //get all Screens
    @GetMapping("/getall")
    public ResponseEntity<List<ScreenResponseDto>> getAllScreens() {
        List<ScreenResponseDto> res = screenService.getAllScreens();
        return ResponseEntity.ok(res);
    }


    //get by id
    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<ScreenResponseDto>> getScreensByTheaterId(@PathVariable Long theaterId) {
        List<ScreenResponseDto> res = screenService.getScreensByTheaterId(theaterId);
        return ResponseEntity.ok(res);
    }


    //Delete Screen By ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreenById(id);
        return ResponseEntity.ok("Screen Deleted Successfully..");
    }


}