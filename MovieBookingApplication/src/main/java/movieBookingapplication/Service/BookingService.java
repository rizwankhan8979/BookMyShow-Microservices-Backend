package movieBookingapplication.Service;

import movieBookingapplication.Dtos.NotfyDto.TicketNotificationRequestDto;
import movieBookingapplication.Dtos.PaymentDtos.PaymentOrderRequestDto;
import movieBookingapplication.Dtos.RequestDtos.BookingRequestDto;
import movieBookingapplication.Dtos.ResponseDtos.BookingResponseDto;
import movieBookingapplication.Entity.Booking;
import movieBookingapplication.Entity.Show;
import movieBookingapplication.Entity.ShowSeat;
import movieBookingapplication.Entity.User;
import movieBookingapplication.Enum.BookingStatus;
import movieBookingapplication.Enum.ShowSeatStatus;
import movieBookingapplication.ExceptionHandler.RequiredFiledsExcetion;
import movieBookingapplication.ExceptionHandler.ResourceAlreadyPresentException;
import movieBookingapplication.ExceptionHandler.ResourceNotFoundException;
import movieBookingapplication.ExceptionHandler.SeatUnavailableException;
import movieBookingapplication.Feign_Interfaces.NotificationClient;
import movieBookingapplication.Feign_Interfaces.PaymentClient;
import movieBookingapplication.Repository.BookingRepository;
import movieBookingapplication.Repository.ShowRepository;
import movieBookingapplication.Repository.ShowSeatRepository;
import movieBookingapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private NotificationClient notificationClient;


    public BookingResponseDto mapToDto(Booking booking){

        BookingResponseDto res = new BookingResponseDto();
        res.setId(booking.getId());
        res.setBookingTime(booking.getBookingTime());
        res.setPrice(booking.getPrice());
        res.setBookingStatus(booking.getBookingStatus());
        res.setNumberOfSeats(booking.getNumberOfSeats());
        res.setSeatsNumbers(booking.getSeatsNumbers());
        res.setRazorpayOrderId(booking.getRazorpayOrderId());//here add booking id

        // User Details
        res.setUserId(booking.getUser().getId());
        res.setUsername(booking.getUser().getUsername());

        // Show Details
        res.setShowId(booking.getShow().getId());
        res.setShowTime(booking.getShow().getShowTime());

        // Movie Details
        res.setMovieId(booking.getShow().getMovie().getId());
        res.setMovieTitle(booking.getShow().getMovie().getTitle());

        // Screen Details
        res.setScreenId(booking.getShow().getScreen().getId());
        res.setScreenName(booking.getShow().getScreen().getScreenName());

        return res;
    }



    public BookingResponseDto addBooking(BookingRequestDto dto) {

        if (dto.getNumberOfSeats() == null || dto.getNumberOfSeats() <= 0) {
            throw new RequiredFiledsExcetion("numberOfSeats must be provided and greater than 0");
        }

        Show show=showRepository.findById(dto.getShowId()).orElseThrow(()->
                new ResourceNotFoundException("Show Not Found "));

        if(!isSeatsAvailable(show.getId(), dto.getNumberOfSeats())){
            throw new SeatUnavailableException("Not Enough Seats are Available");
        }

        if(dto.getSeatsNumbers() == null || dto.getSeatsNumbers().size() != dto.getNumberOfSeats()){
            throw new RequiredFiledsExcetion("Seat Number list size and Number of Seats Must be Equals..");
        }

        validateDuplicateSeats(show.getId(), dto.getSeatsNumbers());

// Spring Security Context se Logged-in User nikalna:
        Object principal = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User user;
        if (principal instanceof User) {
            user = (User) principal;
        } else {
            String currentUsername = org.springframework.security.core.context.SecurityContextHolder
                    .getContext().getAuthentication().getName();
            user = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found: " + currentUsername));
        }

        Booking booking=new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setNumberOfSeats(dto.getNumberOfSeats());
        booking.setPrice(calculateTotalAmount(show.getPrice(), dto.getNumberOfSeats()));
        booking.setSeatsNumbers(dto.getSeatsNumbers());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setBookingTime(dto.getBookingTime());

        // === PAYMENT GATEWAY CALL ===
        PaymentOrderRequestDto paymentDto = new PaymentOrderRequestDto();
        paymentDto.setName(user.getUsername());
        paymentDto.setEmail(user.getEmail());
        paymentDto.setPhoneNumber("9999999999"); // ya default phone number
        paymentDto.setMovieTitle("Movie Ticket: " + show.getMovie().getTitle());
        paymentDto.setAmount(booking.getPrice());

        // Call paymentGateway Microservice via FeignClient
        String razorpayResponse = paymentClient.createOrder(paymentDto);

        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            com.fasterxml.jackson.databind.JsonNode jsonNode = mapper.readTree(razorpayResponse);
            String razorpayOrderId = jsonNode.get("id").asText();
            booking.setRazorpayOrderId(razorpayOrderId);
        } catch (Exception e) {
            throw new RuntimeException("Razorpay Order ID parse karne me error aaya", e);
        }

        Booking savedBooking=bookingRepository.save(booking);
        return mapToDto(savedBooking);
    }



    public boolean isSeatsAvailable(Long showId, Integer numberOfSeats){

        if (numberOfSeats == null || numberOfSeats <= 0) {
            return false;
        }

        Show show=showRepository.findById(showId).orElseThrow(()->
                new ResourceNotFoundException("Show Not Found "));

        int bookedSeats = show.getBookings().stream()
                .filter(booking -> booking.getBookingStatus() != BookingStatus.CANCEL)
                .mapToInt(b -> (b.getNumberOfSeats() != null) ? b.getNumberOfSeats() : 0)
                .sum();

        return (show.getScreen().getCapacity() - bookedSeats) >= numberOfSeats;
    }


    public void validateDuplicateSeats(Long showId, List<String> seatNumbers){
        Show show=showRepository.findById(showId).orElseThrow(()->
                new ResourceNotFoundException("Show Not Found "));

        //here we got the Occupiedf Sears
        Set<String> occupiedSeats=show.getBookings().stream()
                .filter(booking-> booking.getBookingStatus()!=BookingStatus.CANCEL)
                .flatMap(booking->booking.getSeatsNumbers().stream())
                .collect(Collectors.toSet());

        List<String> duplicateSeats=seatNumbers.stream()
                .filter(occupiedSeats::contains)
                .collect(Collectors.toList());

        if(!duplicateSeats.isEmpty()){
            throw new ResourceAlreadyPresentException("Seats are Already Done here..");
        }
    }

    public Double calculateTotalAmount(Double price, Integer numberofSeats){
        return price*numberofSeats;
    }




    //2nd Apis methode
    public List<BookingResponseDto> getAllBookings() {
        List<Booking> res=bookingRepository.findAll();

        if(res.isEmpty()){
            throw new ResourceNotFoundException("Bookings Not Found.");
        }

        List<BookingResponseDto> list=new ArrayList<>();
        for(Booking booking : res){
            BookingResponseDto bb=mapToDto(booking);
            list.add(bb);
        }
        return list;
    }



    public List<BookingResponseDto> getBookingByUserId(Long id) {
        List<Booking> list=bookingRepository.findByUserId(id);

        if(list.isEmpty()){
            throw new ResourceNotFoundException("Bookings Not Found for User ID: " + id);
        }

        List<BookingResponseDto> dtos=new ArrayList<>();
        for(Booking booking : list){
            BookingResponseDto bb=mapToDto(booking);
            dtos.add(bb);
        }
        return dtos;
    }


    public List<BookingResponseDto> getbookingByShowId(Long id) {
        List<Booking> list=bookingRepository.findByShowId(id);

        if(list.isEmpty()){
            throw new ResourceNotFoundException("Bookings Not Found for Show ID: " + id);
        }

        List<BookingResponseDto> dtos=new ArrayList<>();
        for(Booking booking : list){
            BookingResponseDto bb=mapToDto(booking);
            dtos.add(bb);
        }
        return dtos;
    }




    public BookingResponseDto VerifyAndConfirmBooking(Long bookingId,String paymentId, String status) {

        Booking booking=bookingRepository.findById(bookingId).orElseThrow(
                ()->new ResourceNotFoundException("Booking Not Found with ID: " + bookingId));

        if(booking.getBookingStatus()!=BookingStatus.PENDING){
            throw new RequiredFiledsExcetion("Booking is not in PENDING state!");
        }

        // 1. paymentGateway microservice me order status update karein FeignClient se
        String orderId = booking.getRazorpayOrderId(); // PENDING status me saved tha
        paymentClient.updateOrderStatus(paymentId, orderId, status);


        // 2. Agar payment SUCCESS hai toh CONFIRMED karein, warna CANCEL
        if ("SUCCESS".equalsIgnoreCase(status)) {
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            booking.setRazorpayPaymentId(paymentId);
            // Update show seats status to BOOKED in DB
            if (booking.getShow() != null && booking.getSeatsNumbers() != null) {
                List<ShowSeat> showSeats = showSeatRepository.findByShowIdAndSeatNumberIn(
                        booking.getShow().getId(),
                        booking.getSeatsNumbers()
                );
                for (ShowSeat ss : showSeats) {
                    ss.setStatus(ShowSeatStatus.BOOKED);
                    ss.setLockTimestamp(null);
                }
                showSeatRepository.saveAll(showSeats);
            }


            // === TICKET CONFIRMATION EMAIL SEND LOGIC ===
            try {
                TicketNotificationRequestDto mailReq =
                        new TicketNotificationRequestDto(
                                booking.getUser().getEmail(),
                                booking.getUser().getUsername(),
                                booking.getId(),
                                booking.getShow().getMovie().getTitle(),
                                booking.getShow().getScreen().getTheater().getTheatername(),
                                booking.getShow().getScreen().getScreenName(),
                                booking.getShow().getShowTime().toString(),
                                booking.getSeatsNumbers(),
                                booking.getPrice(),
                                paymentId
                        );
                notificationClient.sendTicketConfirmation(mailReq);
            } catch (Exception e) {
                System.err.println("Failed to send Ticket Email: " + e.getMessage());
            }

        } else {
            booking.setBookingStatus(BookingStatus.CANCEL);
        }

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDto(savedBooking);

//        booking.setBookingStatus(BookingStatus.CONFIRMED);
//
//        // Update show seats status to BOOKED in DB
//        if (booking.getShow() != null && booking.getSeatsNumbers() != null) {
//
//            List<ShowSeat> showSeats =
//                    showSeatRepository.findByShowIdAndSeatNumberIn(
//                            booking.getShow().getId(),
//                            booking.getSeatsNumbers()
//                    );
//
//            for (ShowSeat ss : showSeats) {
//                ss.setStatus(ShowSeatStatus.BOOKED);
//                ss.setLockTimestamp(null);
//            }
//            showSeatRepository.saveAll(showSeats);
//        }

    }



    public BookingResponseDto  cancelBookingById(Long bookingId) {
        Booking booking=bookingRepository.findById(bookingId).orElseThrow(
                ()->new ResourceNotFoundException("Booking Not Found with ID: " + bookingId));
        validateCancellation(booking);

        booking.setBookingStatus(BookingStatus.CANCEL);

        // Release show seats back to AVAILABLE in DB
        if (booking.getShow() != null && booking.getSeatsNumbers() != null) {

            List<ShowSeat> showSeats =
                    showSeatRepository.findByShowIdAndSeatNumberIn(
                            booking.getShow().getId(),
                            booking.getSeatsNumbers()
                    );

            for (ShowSeat ss : showSeats) {
                ss.setStatus(ShowSeatStatus.AVAILABLE);
                ss.setLockTimestamp(null);
            }
            showSeatRepository.saveAll(showSeats);
        }

        Booking savedBooking = bookingRepository.save(booking);

        return mapToDto(savedBooking);
    }




    public void validateCancellation(Booking booking){

        LocalDateTime showTime=booking.getShow().getShowTime();
        LocalDateTime deadlineTime=showTime.minusHours(2);

        if(LocalDateTime.now().isAfter(deadlineTime)){
            throw new RequiredFiledsExcetion("Cannot cancel booking less than 2 hours before showtime!");
        }

        if(booking.getBookingStatus()==BookingStatus.CANCEL){
            throw new RequiredFiledsExcetion("Booking is already cancelled!");
        }

    }


    public List<BookingResponseDto> getBookingByBookingStatus(BookingStatus bookingStatus) {
        List<Booking> bookings = bookingRepository.findByBookingStatus(bookingStatus);
        if (bookings.isEmpty()) {
            throw new ResourceNotFoundException("No Bookings Found with Status: " + bookingStatus);
        }

        List<BookingResponseDto> responseList = new ArrayList<>();
        for (Booking booking : bookings) {

            BookingResponseDto response = mapToDto(booking);

            responseList.add(response);
        }
        return responseList;
    }
}
