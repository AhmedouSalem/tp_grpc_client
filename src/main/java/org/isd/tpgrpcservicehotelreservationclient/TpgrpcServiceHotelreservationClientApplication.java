package org.isd.tpgrpcservicehotelreservationclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"org.isd.tpgrpcservicehotelreservationclient.service",
		"org.isd.tpgrpcservicehotelreservationclient.config",
		"org.isd.tpgrpcservicehotelreservationclient.cli"
})
public class TpgrpcServiceHotelreservationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpgrpcServiceHotelreservationClientApplication.class, args);
	}

}
