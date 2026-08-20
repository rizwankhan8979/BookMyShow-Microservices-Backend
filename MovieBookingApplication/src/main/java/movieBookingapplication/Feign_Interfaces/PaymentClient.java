package movieBookingapplication.Feign_Interfaces;


import movieBookingapplication.Dtos.PaymentDtos.PaymentOrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "paymentGateway") // Eureka service name
public interface PaymentClient {

    @PostMapping("/api/payment/create-order")
    String createOrder(@RequestBody PaymentOrderRequestDto paymentOrderDto);

    @PostMapping("/api/payment/update-order")
    String updateOrderStatus(@RequestParam("paymentId") String paymentId,
                             @RequestParam("orderId") String orderId,
                             @RequestParam("status") String status);

}