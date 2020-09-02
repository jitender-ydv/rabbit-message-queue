package com.jeet.lil.learningspringboot;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.any;

@SpringBootApplication
@EnableSwagger2
public class LearningSpringbootApplication {
        @Bean
        public Docket api(){
            return new Docket(DocumentationType.SWAGGER_2).groupName("User").select()
                    .apis(RequestHandlerSelectors.basePackage("com.jeet.lil.learningspringboot"))
                    .paths(any()).build().apiInfo(new ApiInfo("User Details",
                            "Service for getting details of users and create them","1.0.0",
                            null,"Jitender Yadav",
                            null,null) );
        }

    @Value("${amqp.queue.name}")
    private String queueName;

    @Value("${amqp.exchange.name}")
    private String exchangeName;

    @Bean
    public Queue queue(){
        return new Queue(queueName, false);
    }
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange topicExchange){
        return BindingBuilder.bind(queue).to(topicExchange).with(queueName);
    }


    public static void main(String[] args) {
        SpringApplication.run(LearningSpringbootApplication.class, args);

    }
}
