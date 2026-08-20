package movieBookingapplication.Service;

import movieBookingapplication.Dtos.RequestDtos.TheaterRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.ScreenResponseDto;
import movieBookingapplication.Dtos.ResponseDtos.TheaterResponseDto;
import movieBookingapplication.Entity.Screen;
import movieBookingapplication.Entity.Theater;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.Repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    @Autowired
    private TheaterRepository theaterRepository;


    //mapper ,methode here
    public TheaterResponseDto mapToDto(Theater theater){

        TheaterResponseDto res=new TheaterResponseDto();//here fetch the theater dtos response
        //here set value of the theaters response
        res.setId(theater.getId());
        res.setTheatername(theater.getTheatername());
        res.setAddress(theater.getAddress());
        res.setLocation(theater.getLocation());

        //here create ArrayList to hold the screen data
        List<ScreenResponseDto> list=new ArrayList<>();

        // Null check add kiya gaya hai
        if (theater.getScreens() != null) {
            for(Screen screen : theater.getScreens()){
                ScreenResponseDto dto=new ScreenResponseDto();
                dto.setId(screen.getId());
                dto.setScreenName(screen.getScreenName());
                dto.setCapacity(screen.getCapacity());
                dto.setTheaterId(screen.getTheater().getId());
                dto.setTheaterName(screen.getTheater().getTheatername());
                list.add(dto);
            }
        }
        res.setScreens(list);
        return res;
    }


    public TheaterResponseDto addTheater(TheaterRequestDto dto) {

        Theater theater=new Theater();

        theater.setTheatername(dto.getTheatername());
        theater.setAddress(dto.getAddress());
        theater.setLocation(dto.getLocation());
        Theater savedTheater=theaterRepository.save(theater);
        return mapToDto(savedTheater);
    }

    public List<TheaterResponseDto> getTheaterBylocation(String location) {

        Optional<List<Theater>> list=theaterRepository.findByLocation(location);
        if(list.isEmpty()) {
            throw new ResourceNotFoundException("Theater Not Found With " + location);
        }

        List<Theater> theaters=list.get();
        List<TheaterResponseDto> responseList = new ArrayList<>();
        for(Theater theater : theaters){//theaters add one by one in mapper methode
            TheaterResponseDto res=mapToDto(theater);
            responseList.add(res);
        }
        return responseList;
    }



    public TheaterResponseDto updateTheater(Long id, TheaterRequestDto dto) {

        Theater theater=theaterRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No Theater Found with "+id));

        theater.setTheatername(dto.getTheatername());
        theater.setAddress(dto.getAddress());
        theater.setLocation(dto.getLocation());
        Theater savedTheater=theaterRepository.save(theater);
        return mapToDto(savedTheater);

    }

    public void deleteById(Long id) {
        Theater theater=theaterRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No Theater Found with "+id));
        theaterRepository.deleteById(id);
    }
}
