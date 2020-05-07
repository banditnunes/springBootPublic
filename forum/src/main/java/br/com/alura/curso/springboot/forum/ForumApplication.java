package br.com.alura.curso.springboot.forum;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
@EnableCaching
@EnableSwagger2
public class ForumApplication {
	private static final Logger logger= LoggerFactory.getLogger(ForumApplication.class);

	public static void main(String[] args) {
		logger.info("Iniciando API de Gerenciamento do Forum ...");
		SpringApplication.run(ForumApplication.class, args);
		logger.info("API iniciada e pronta para receber requisições");
	}

}
