package com.jeet.lil.learningspringboot;

import org.springframework.data.repository.CrudRepository;
import com.jeet.lil.learningspringboot.models.Users;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

}