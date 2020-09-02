package com.jeet.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserDataProcessor {
    private final ObjectMapper objectMapper;
    private static final Logger LOGGER= LoggerFactory.getLogger(UserDataProcessor.class);

    @Autowired
    public UserDataProcessor(ObjectMapper objectMapper){
        super();
        this.objectMapper=objectMapper;

    }
    public void receiveMessage(String userJson){
        LOGGER.info("Message Received");
        try {
            Users users=this.objectMapper.readValue(userJson, Users.class);
            LOGGER.info("user details received : "+ users.getName());
        } catch (IOException e) {
            LOGGER.info("Json IO Exception Caught",e);
        }
    }
}
