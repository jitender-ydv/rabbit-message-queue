package com.jeet.lil.learningspringboot.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeet.lil.learningspringboot.UserRepository;
import com.jeet.lil.learningspringboot.models.Users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Component
@Controller
@Api(value = "Users", tags = ("users"))
@RequestMapping(path = "/demo")
public class MainController {

    @Value("${amqp.queue.name}")
    private String queueName;

    private static final Logger LOGGER= LoggerFactory.getLogger(MainController.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MainController(RabbitTemplate rabbitTemplate, ConfigurableApplicationContext context, ObjectMapper objectMapper){
        super();
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @ApiOperation(value = "Adding a new user to the Users Table ",nickname = "add a User")
    @PostMapping(path = "/add")
    public @ResponseBody Users addNewUser(@RequestParam String name, @RequestParam String email){
        Users newUser = new Users();
        newUser.setName(name);
        newUser.setEmail(email);
        userRepository.save(newUser);
        return newUser;
    }
    @ApiOperation(value = "Get all the users available in the system",nickname = "get all users")
    @GetMapping(path = "/getAll")
    public @ResponseBody List<Users> getAllUsers(){
        List<Users> users= (List<Users>) userRepository.findAll();
        users.forEach(user -> {
            LOGGER.info("Sending Message");
            System.out.println("user-data: \t"+ user.getName());
            try {
                String jsonString =objectMapper.writeValueAsString(user);
                rabbitTemplate.convertAndSend(queueName, jsonString);
            } catch (JsonProcessingException e) {
                LOGGER.error("Parsing Exception", e);
            }
        });
        return users;
    }
}
