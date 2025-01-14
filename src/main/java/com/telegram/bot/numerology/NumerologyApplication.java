package com.telegram.bot.numerology;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NumerologyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NumerologyApplication.class, args);
	}

}
