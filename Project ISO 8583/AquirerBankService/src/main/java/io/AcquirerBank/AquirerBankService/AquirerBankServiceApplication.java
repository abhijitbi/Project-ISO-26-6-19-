package io.AcquirerBank.AquirerBankService;

import io.AcquirerBank.AquirerBankService.resource.AcquirerRestController;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AquirerBankServiceApplication {
	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){

		return new    RestTemplate();
	}

	public static void main(String[] args) throws Exception{

		SpringApplication.run(AquirerBankServiceApplication.class, args);


	}

}
