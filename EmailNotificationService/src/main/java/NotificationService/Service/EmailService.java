package NotificationService.Service;

import NotificationService.Dtos.RegistrationSuccessRequest;
import NotificationService.Dtos.TicketNotificationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @org.springframework.beans.factory.annotation.Value("${spring.mail.username:rizwankhan60hy@gmail.com}")
    private String fromEmail;

    // SEND OTP
    public String sendOtp(String email, String otp){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("BookShowNow - Your OTP Verification Code");
        message.setText(
                "Hello User,\n\n"
                        + "Your BookShowNow Account Verification OTP is : " + otp
                        + "\n\nThis OTP is valid for 5 minutes."
                        + "\n\nThank You,\nBookShowNow Team"
        );

        javaMailSender.send(message);

        return "OTP Sent Successfully";
    }




    // REGISTRATION SUCCESS
    public String registrationSuccessMail(RegistrationSuccessRequest request){

        String name = request.getUsername() != null ? request.getUsername() : "there";
        String email = request.getEmail() != null ? request.getEmail() : "";

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("You're all set, " + name + "! \uD83C\uDFAC");
        message.setText(
                "Hey " + name + ",\n\n"
                + "Welcome to BookShowNow! \uD83C\uDF89\n\n"
                + "You've just unlocked the best movie booking\n"
                + "experience in India. From Bollywood blockbusters\n"
                + "to live concerts \u2014 it all starts here.\n\n"
                + "\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n"
                + "YOUR ACCOUNT\n"
                + "   Name  : " + name + "\n"
                + "   Email : " + email + "\n"
                + "\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n\n"
                + " WHAT YOU CAN DO NOW\n\n"
                + "   \uD83C\uDFA5  Book IMAX, 4DX & Dolby Atmos tickets\n"
                + "   \uD83C\uDFDF  Choose your favourite seats in real-time\n"
                + "   \uD83D\uDCF2  Get instant E-Tickets directly on email\n"
                + "   \uD83C\uDFAD  Explore Live Events & Concerts near you\n\n"
                + "\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n"
                + "\uD83D\uDE80 Start Exploring: http://localhost:3000\n"
                + "\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n\n"
                + "\u26A0\uFE0F  Didn't create this account?\n"
                + "   Contact us: support@bookshownow.com\n\n"
                + "With \uD83C\uDFAC from the BookShowNow Team\n"
                + "\u00A9 2026 BookShowNow. All rights reserved.\n"
                + "Mumbai, India\n"
                + "\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501"
        );

        javaMailSender.send(message);

        return "Registration Mail Sent";
    }





    public String sendTicketConfirmationMail(TicketNotificationRequestDto req) {
        if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
            return "Email is empty, cannot send ticket";
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(req.getEmail().trim());
            message.setSubject("Ticket Confirmed - Booking ID: #" + req.getBookingId() + " - " + req.getMovieTitle());

            String text = "Dear " + (req.getUsername() != null && !req.getUsername().trim().isEmpty() ? req.getUsername() : "Customer") + ",\n\n"
                    + "Thank you for booking with BookShowNow. Your movie ticket booking has been confirmed successfully.\n\n"
                    + "BOOKING DETAILS\n"
                    + "--------------------------------------------------\n"
                    + "Booking ID     : #" + req.getBookingId() + "\n"
                    + "Movie Name     : " + req.getMovieTitle() + "\n"
                    + "Theater        : " + (req.getTheaterName() != null ? req.getTheaterName() : "Cinema Hall") + "\n"
                    + "Screen         : " + (req.getScreenName() != null ? req.getScreenName() : "Screen 1") + "\n"
                    + "Show Time      : " + (req.getShowTime() != null ? req.getShowTime() : "N/A") + "\n"
                    + "Seats Booked   : " + (req.getSeats() != null && !req.getSeats().isEmpty() ? String.join(", ", req.getSeats()) : "N/A") + "\n"
                    + "Total Paid     : Rs. " + (req.getAmount() != null ? String.format("%.2f", req.getAmount()) : "0.00") + "\n\n"
                    + "PAYMENT DETAILS\n"
                    + "--------------------------------------------------\n"
                    + "Payment ID     : " + (req.getPaymentId() != null ? req.getPaymentId() : "N/A") + "\n"
                    + "Payment Status : SUCCESS (Paid via Razorpay)\n"
                    + "--------------------------------------------------\n\n"
                    + "Please present this Booking ID or email at the cinema entrance.\n\n"
                    + "Regards,\n"
                    + "BookShowNow Team\n"
                    + "Customer Support: rizwankhan.officialit@gmail.com";

            message.setText(text);
            javaMailSender.send(message);
            System.out.println("Ticket confirmation email sent successfully to: " + req.getEmail());
            return "Ticket Email Sent Successfully";
        } catch (Exception e) {
            System.err.println("Error sending ticket email: " + e.getMessage());
            return "Failed to send ticket email: " + e.getMessage();
        }
    }


    public String sendPasswordResetOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("BookShowNow - Password Reset Verification Code \uD83D\uDD12");
        message.setText(
                "Hello User,\n\n"
                        + "We received a request to reset your BookShowNow account password.\n\n"
                        + "Your Password Reset OTP is : " + otp + "\n\n"
                        + "This OTP is valid for 5 minutes. If you did not request a password reset, please ignore this email.\n\n"
                        + "Thank You,\nBookShowNow Team"
        );
        javaMailSender.send(message);
        return "Password Reset Mail Sent";
    }

    public String sendPasswordResetSuccessMail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("BookShowNow - Password Changed Successfully! \uD83D\uDD12");
        message.setText(
                "Hello " + username + ",\n\n"
                        + "Your BookShowNow account password has been changed successfully.\n\n"
                        + "If you did not perform this action, please contact our support team immediately at support@bookshownow.com.\n\n"
                        + "You can now sign in to your account and continue booking your favorite movies.\n\n"
                        + "Enjoy the application!\n\n"
                        + "Thank You,\nBookShowNow Team"
        );
        javaMailSender.send(message);
        return "Password Reset Success Mail Sent";
    }

}
