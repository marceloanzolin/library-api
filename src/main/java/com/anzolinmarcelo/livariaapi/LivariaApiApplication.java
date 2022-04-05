package com.anzolinmarcelo.livariaapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LivariaApiApplication {

	@Bean
	public ModelMapper modelMapper() {
		//cria um singleton para servir toda a aplicação
		return new ModelMapper();
	}
	
	public static void main(String[] args) {
		SpringApplication.run(LivariaApiApplication.class, args);
	}

}
