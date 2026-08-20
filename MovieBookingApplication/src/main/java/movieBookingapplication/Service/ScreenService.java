package movieBookingapplication.Service;

import movieBookingapplication.Dtos.RequestDtos.ScreenRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.ScreenResponseDto;
import movieBookingapplication.Entity.Screen;
import movieBookingapplication.Entity.Theater;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.Repository.ScreenRepository;
import movieBookingapplication.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheaterRepository theaterRepository;


    //mapper methode Convert EntityToDto
    public ScreenResponseDto mapToDto(Screen screen){
        ScreenResponseDto res=new ScreenResponseDto();
        res.setId(screen.getId());
        res.setScreenName(screen.getScreenName());
        res.setCapacity(screen.getCapacity());
        res.setTheaterId(screen.getTheater().getId());
        res.setTheaterName(screen.getTheater().getTheatername());
        return res;
    }

    //add Screen
    public ScreenResponseDto addScreen(ScreenRequestDto dto) {
        Theater theater = theaterRepository.findById(dto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater Not Found with ID: "
                        + dto.getTheaterId()));

        Screen screen = new Screen();
        screen.setScreenName(dto.getScreenName());
        screen.setCapacity(dto.getCapacity());
        screen.setTheater(theater);
        Screen savedScreen=screenRepository.save(screen);
        return  mapToDto(savedScreen);
    }



    // fetch all screens
    public List<ScreenResponseDto> getAllScreens() {
        List<Screen> list=screenRepository.findAll();

        List<ScreenResponseDto> res=new ArrayList<>();
        for(Screen screen : list){
            ScreenResponseDto dto=mapToDto(screen);
            res.add(dto);
        }
        return res;
    }



    // GET SCREEN BY ID
    public List<ScreenResponseDto> getScreensByTheaterId(Long theaterId) {
        List<Screen> res=screenRepository.findByTheaterId(theaterId);
        List<ScreenResponseDto> list=new ArrayList<>();
        for(Screen screen:  res){
            ScreenResponseDto dto=mapToDto(screen);
            list.add(dto);
        }
        return list;
    }

    //delete bye id
    public void deleteScreenById(Long id) {
        screenRepository.deleteById(id);
    }
}

