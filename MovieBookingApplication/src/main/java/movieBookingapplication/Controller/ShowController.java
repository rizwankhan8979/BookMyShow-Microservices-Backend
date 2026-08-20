package movieBookingapplication.Controller;

import movieBookingapplication.Dtos.RequestDtos.ShowRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.ShowResponseDto;
import movieBookingapplication.Entity.Show;
import movieBookingapplication.Service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/show")
public class ShowController {


    @Autowired
    private ShowService showService;

    @PostMapping("/create")
    public ResponseEntity<ShowResponseDto> createShow(@RequestBody ShowRequestDto showRequestDto ){
        ShowResponseDto res=showService.addShow(showRequestDto);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(res);

    }

    @GetMapping("/getall")
    public ResponseEntity<List<ShowResponseDto>> getAllShows(){
        List<ShowResponseDto> list=showService.getAllShows();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getshowbymovie/{id}")
    public ResponseEntity<List<ShowResponseDto>> getShowByMovie(@PathVariable Long id){
        List<ShowResponseDto> res=showService.getShowByMovie(id);
        return ResponseEntity.ok(res);

    }

    @GetMapping("/getbytheater/{id}")
    public ResponseEntity<List<ShowResponseDto>> getByTheater(@PathVariable Long id){
        List<ShowResponseDto> res=showService.getShowByTheater(id);
        return ResponseEntity.ok(res);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ShowResponseDto> updateShow(@PathVariable Long id,
                                           @RequestBody ShowRequestDto dto){
        ShowResponseDto res=showService.updateShowById(id, dto);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/delete/{id}")
    public  ResponseEntity<String> deleteShow(@PathVariable Long id){
        showService.deleteById(id);
        return ResponseEntity.ok("Deleted Show");
    }

}
