package com.system.emails.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.system.emails.model.RoleEntity;
import com.system.emails.model.UserEntity;
import com.system.emails.repository.RoleRepository;
import com.system.emails.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository rolRepo;

    public UserEntity newUser(String nombre, String email, String password) {

        UserEntity user = new UserEntity();
        user.setNombre(nombre);
        user.setEmail(email);
        user.setPassword(password);

        // obtener rol CLIENTE
        RoleEntity rolCliente = rolRepo.findByNombre("CLIENTE");

        Set<RoleEntity> roles = new HashSet<>();
        roles.add(rolCliente);

        user.setRoles(roles);

        return userRepo.save(user);
    }

     public List<UserEntity> findAllUsers() {
        return userRepo.findAll();
    }

    public List<UserEntity> findAllUsersByRole(String roleName) {
       return userRepo.findDistinctByRolesNombre(roleName);
        }
}
