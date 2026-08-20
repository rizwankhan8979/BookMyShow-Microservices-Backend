package movieBookingapplication.Service;

import movieBookingapplication.Dtos.ResponseDtos.ShowSeatResponse;
import movieBookingapplication.Entity.ShowSeat;
import movieBookingapplication.Enum.ShowSeatStatus;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.ExceptionHandler.SeatUnavailableException;
import movieBookingapplication.Repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowSeatService {

    @Autowired
    private ShowSeatRepository showSeatRepository;


    public ShowSeatResponse mapToDto(ShowSeat showSeat){
        ShowSeatResponse res=new ShowSeatResponse();
        res.setId(showSeat.getId());
        res.setSeatNumber(showSeat.getSeatNumber());
        res.setSeatType(showSeat.getSeat().getSeatType());
        res.setPrice(showSeat.getPrice());
        res.setStatus(showSeat.getStatus());
        res.setScreenName(showSeat.getSeat().getScreen().getScreenName());
        res.setShowId(showSeat.getShow().getId());
        res.setSeatId(showSeat.getSeat().getId());
        return res;
    }

    //get all booking ShowSeats by seatId
    public List<ShowSeatResponse> getShowSeatsByShowId(Long showId) {
        unlockExpiredSeats(showId);
        List<ShowSeat> seat=showSeatRepository.findByShowId(showId);

        List<ShowSeatResponse> list=new ArrayList<>();
        for(ShowSeat seats : seat){
            ShowSeatResponse res=mapToDto(seats);
            list.add(res);
        }
        return list;
    }


    //Lock Seats for 10 minutes
    public List<ShowSeatResponse> lockSeats(List<Long> showSeatIds) {
        List<ShowSeat> seatsToLock = showSeatRepository.findAllById(showSeatIds);

        for (ShowSeat showSeat : seatsToLock) {
            if (showSeat.getStatus() != ShowSeatStatus.AVAILABLE) {
                throw new SeatUnavailableException("Seat " +
                        showSeat.getSeatNumber() + " is not AVAILABLE for locking!");
            }
            showSeat.setStatus(ShowSeatStatus.LOCKED);
            showSeat.setLockTimestamp(LocalDateTime.now());
        }

        List<ShowSeat> savedSeats = showSeatRepository.saveAll(seatsToLock);
        List<ShowSeatResponse> responseList = new ArrayList<>();
        for (ShowSeat showSeat : savedSeats) {
            responseList.add(mapToDto(showSeat));
        }
        return responseList;
    }


    // Helper: Unlock seats locked for more than 10 minutes
    private void unlockExpiredSeats(Long showId) {
        List<ShowSeat> lockedSeats = showSeatRepository.findByShowIdAndStatus(showId, ShowSeatStatus.LOCKED);
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);

        for (ShowSeat seat : lockedSeats) {
            if (seat.getLockTimestamp() != null && seat.getLockTimestamp().isBefore(tenMinutesAgo)) {
                seat.setStatus(ShowSeatStatus.AVAILABLE);
                seat.setLockTimestamp(null);
                showSeatRepository.save(seat);
            }
        }
    }



}
