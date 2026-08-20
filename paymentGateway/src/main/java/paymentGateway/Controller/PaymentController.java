package paymentGateway.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import paymentGateway.Entity.PaymentOrder;
import paymentGateway.Service.PaymentService;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")//this is order created
    public ResponseEntity<String> OrderCreated(@RequestBody PaymentOrder paymentOrder) {

        System.out.println("Inside Controller......");
        try {
            String responsOrder = paymentService.createOrder(paymentOrder);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(responsOrder);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error Creating Order");
        }
    }


    @PostMapping("/update-order")
    public ResponseEntity<String> updateOrderStatus(@RequestParam String paymentId,
                                                    @RequestParam(required = false) String orderId,
                                                    @RequestParam(defaultValue = "SUCCESS") String status,
                                                    @RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String movieTitle,
                                                    @RequestParam(required = false) Double amount){

        paymentService.updateOrderStatus(paymentId, orderId, status, email, name, movieTitle, amount);
        System.out.println("Order status updated successfully for paymentId: " + paymentId);
        return ResponseEntity.ok("Order Placed Successfully & Confirmation Email Processed");
    }


}
