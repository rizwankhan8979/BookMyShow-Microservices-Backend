package paymentGateway.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username:rizwankhan60hy@gmail.com}")
    private String fromEmail;

    public void sendEmail(String toEmail, String name, String itemOrMovie, Double amount, String paymentId, String orderId){
        if (toEmail == null || toEmail.trim().isEmpty()) {
            System.err.println("Cannot send email: toEmail is empty.");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail.trim());
            message.setSubject("💳 Payment Successful - " + (itemOrMovie != null ? itemOrMovie : "Movie Ticket Booking"));

            String body = "Hi " + (name != null ? name : "Customer") + ",\n\n"
                    + "🎉 Your payment has been processed successfully!\n\n"
                    + "PAYMENT DETAILS:\n"
                    + "--------------------------------------------------\n"
                    + "Movie / Item   : " + (itemOrMovie != null ? itemOrMovie : "Movie Ticket Booking") + "\n"
                    + "Amount Paid    : ₹" + (amount != null ? String.format("%.2f", amount) : "0.00") + "\n"
                    + "Payment ID     : " + (paymentId != null ? paymentId : "N/A") + "\n"
                    + "Order ID       : " + (orderId != null ? orderId : "N/A") + "\n"
                    + "Status         : SUCCESSFUL\n"
                    + "--------------------------------------------------\n\n"
                    + "Thank you for booking with BookShowNow!\n"
                    + "Enjoy your show!\n\n"
                    + "Best Regards,\n"
                    + "BookShowNow Team";

            message.setText(body);
            javaMailSender.send(message);
            System.out.println("Payment confirmation email sent successfully to: " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending payment confirmation email to " + toEmail + ": " + e.getMessage());
        }
    }

    public void sendEmail(String toEmail, String name, String itemOrMovie, Double amount){
        sendEmail(toEmail, name, itemOrMovie, amount, "N/A", "N/A");
    }
}

