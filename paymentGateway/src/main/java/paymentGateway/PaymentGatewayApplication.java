package paymentGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentGatewayApplication.class, args);
		System.out.println("Application Has Been Started..");
	}

}
