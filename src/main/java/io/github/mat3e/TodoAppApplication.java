package io.github.mat3e;

import io.github.mat3e.model.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

//import javax.validation.Validator;

//@Import(TaskConfigurationProperties.class)
@EnableAsync
@SpringBootApplication
@EnableJpaRepositories
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoAppApplication.class, args
		);
	}

	Validator validator(TaskRepository repository){
		return repository.findById(1)
				.map((task) -> new LocalValidatorFactoryBean())
				.orElse(null);
	}

	@Bean
	Validator validator(){
		return new LocalValidatorFactoryBean();
	}



}
