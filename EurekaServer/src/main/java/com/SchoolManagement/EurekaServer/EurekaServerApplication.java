package com.SchoolManagement.EurekaServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.Date;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EurekaServerApplication.class, args);

		System.out.println("SCHOOLMANAGEMENT MICROSERVICE RUNNING AT PORT 8761 INITIALIZED SUCCESSFULLY AT " + new Date());
	}
}

