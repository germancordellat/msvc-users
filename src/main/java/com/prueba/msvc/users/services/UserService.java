package com.prueba.msvc.users.services;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prueba.msvc.users.entities.Role;
import com.prueba.msvc.users.entities.User;
import com.prueba.msvc.users.repositories.RoleRepository;
import com.prueba.msvc.users.repositories.UserRepository;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }
    @Transactional  
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(getRoles());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> update(User user, Long id) {
        

        Optional<User> existingUser = this.findById(id);

        return existingUser.map(userDb -> {
            userDb.setEmail(user.getEmail());
            userDb.setUsername(user.getUsername());
            if(userDb.isEnabled() != null ){
                userDb.setEnabled(user.isEnabled());
            }
            user.setRoles(getRoles());
           
            return Optional.of(userRepository.save(userDb));
        }).orElseGet(() -> Optional.empty());

    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        Optional<Role> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add);
        return roles;
    }

}
