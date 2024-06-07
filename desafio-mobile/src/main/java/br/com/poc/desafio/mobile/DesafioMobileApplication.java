package br.com.poc.desafio.mobile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DesafioMobileApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioMobileApplication.class, args);
	}

}
