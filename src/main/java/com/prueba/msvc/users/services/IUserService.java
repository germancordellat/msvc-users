package com.prueba.msvc.users.services;


import java.util.Optional;

import com.prueba.msvc.users.entities.User;

public interface IUserService {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Iterable<User> findAll();

    void delete(Long id);

    User save(User user);

}
