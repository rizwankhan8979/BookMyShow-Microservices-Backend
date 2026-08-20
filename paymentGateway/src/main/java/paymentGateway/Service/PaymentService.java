package paymentGateway.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import paymentGateway.Entity.PaymentOrder;
import paymentGateway.Repository.PaymentRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {


    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;


    //Create Payment Order
    public String createOrder(PaymentOrder orderDetails) throws RazorpayException {

        System.out.println("Inside Service......");

        //this client through the exception
        RazorpayClient client=new RazorpayClient(keyId, keySecret);

        //We create JSON object for the Razorpay
        JSONObject orderRequest=new JSONObject();
        orderRequest.put("amount", (int)(orderDetails.getAmount()*100));//always take  valey in key value pairs
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + UUID.randomUUID());

        //create order form client side order takes JSON that give upper side
        Order razorpayOrder=client.orders.create(orderRequest);
        System.out.println(razorpayOrder.toString());

        orderDetails.setOrderId(razorpayOrder.get("id"));
        orderDetails.setStatus("CREATED");
        orderDetails.setCreateAt(LocalDateTime.now());

        paymentRepository.save(orderDetails);
        return razorpayOrder.toString();

       // return new JSONObject(razorpayOrder.toString()).toString(4);
    }

    public void updateOrderStatus(String paymentId, String orderId, String status, String fallbackEmail, String fallbackName, String fallbackMovie, Double fallbackAmount) {
        PaymentOrder order = null;
        if (orderId != null && !orderId.trim().isEmpty()) {
            order = paymentRepository.findByOrderId(orderId);
        }

        if (order != null) {
            order.setPaymentId(paymentId);
            order.setStatus(status);
            paymentRepository.save(order);

            if ("SUCCESS".equalsIgnoreCase(status)) {
                emailService.sendEmail(order.getEmail(), order.getName(), order.getCourseName(), order.getAmount(), order.getPaymentId(), order.getOrderId());
            }
        } else {
            System.out.println("Warning: PaymentOrder not found by orderId: " + orderId + ", using fallback details to record and send email.");
            PaymentOrder fallbackOrder = new PaymentOrder();
            fallbackOrder.setOrderId(orderId != null ? orderId : "order_manual");
            fallbackOrder.setPaymentId(paymentId);
            fallbackOrder.setStatus(status);
            fallbackOrder.setEmail(fallbackEmail != null && !fallbackEmail.isEmpty() ? fallbackEmail : "rizwankhan.officialit@gmail.com");
            fallbackOrder.setName(fallbackName != null && !fallbackName.isEmpty() ? fallbackName : "Customer");
            fallbackOrder.setCourseName(fallbackMovie != null && !fallbackMovie.isEmpty() ? fallbackMovie : "Movie Ticket Booking");
            fallbackOrder.setAmount(fallbackAmount != null ? fallbackAmount : 0.0);
            fallbackOrder.setCreateAt(LocalDateTime.now());
            try {
                paymentRepository.save(fallbackOrder);
            } catch (Exception e) {
                System.err.println("Could not save fallback order: " + e.getMessage());
            }

            if ("SUCCESS".equalsIgnoreCase(status)) {
                emailService.sendEmail(fallbackOrder.getEmail(), fallbackOrder.getName(), fallbackOrder.getCourseName(), fallbackOrder.getAmount(), paymentId, orderId);
            }
        }
    }

    public void updateOrderStatus(String paymentId, String orderId, String status) {
        updateOrderStatus(paymentId, orderId, status, null, null, null, null);
    }
}
