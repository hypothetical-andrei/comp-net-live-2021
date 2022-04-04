package cn.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}
	
	@Bean
	public OpenAPI openApi(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI().info(new Info()
				.title("Notes API")
				.version(appVersion)
				.description("This is a note api")
				.termsOfService("http://swagger.io/terms")
				.license(new License().name("Apache 2.0")
						.url("http://sprindoc.org")));
			
	}
}
