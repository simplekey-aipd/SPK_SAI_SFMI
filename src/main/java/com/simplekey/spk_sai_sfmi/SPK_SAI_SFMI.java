package com.simplekey.spk_sai_sfmi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SPK_SAI_SFMI {

	public static void main(String[] args) {
		SpringApplication.run(SPK_SAI_SFMI.class, args);
	}

}
