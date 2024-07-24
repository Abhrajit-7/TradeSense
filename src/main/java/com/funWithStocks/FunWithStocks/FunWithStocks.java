package com.funWithStocks.FunWithStocks;

import com.funWithStocks.FunWithStocks.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@ComponentScan("com.funWithStocks")
@SpringBootApplication
@ServletComponentScan
//@EnableJpaRepositories(basePackages = "com.arrow.Arrow.repository.UserRepository.java")
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class FunWithStocks {

	public static void main(String[] args) {
		SpringApplication.run(FunWithStocks.class, args);
	}

}
