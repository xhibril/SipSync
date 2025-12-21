package com.sipsync.sipsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SipSyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(SipSyncApplication.class, args);
	}

}
