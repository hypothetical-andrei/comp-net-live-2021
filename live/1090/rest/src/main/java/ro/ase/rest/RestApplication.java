package ro.ase.rest;

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
		return new OpenAPI().info(new Info().title("Awesome notes API")
				.version(appVersion)
				.description("This is a sample Notes API")
				.termsOfService("http://swagger.io/terms/")
				.license(new License().name("GPLv3").url("https://www.gnu.org/licenses/gpl-3.0.en.html")));
	}
}
