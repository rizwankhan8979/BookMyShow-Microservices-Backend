package movieBookingapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MovieBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieBookingApplication.class, args);
		System.out.println("Application Has been Started..");
	}

}
