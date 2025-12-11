package com.system.emails.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.system.emails.model.RoleEntity;
import com.system.emails.model.UserEntity;
import com.system.emails.repository.RoleRepository;
import com.system.emails.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final RoleRepository rolRepo;


    @Autowired
    private final PasswordEncoder passwordEncoder;  // <-- inyectar por constructor (Recommended)


    public UserEntity newUser(String nombre, String email, String password) {

        UserEntity user = new UserEntity();
        user.setNombre(nombre);
        user.setEmail(email);
        // Almacenar la contraseña hasheada para que la autenticación sea válida
        user.setPassword(passwordEncoder.encode(password));

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

    
    public UserEntity registrarAdmin(String nombre, String email, String rawPassword) {
    if (userRepo.findByNombre(nombre).isPresent()) {
        throw new RuntimeException("El usuario ya existe");
    }

    UserEntity admin = new UserEntity();
    admin.setNombre(nombre);
    admin.setEmail(email);
    admin.setPassword(passwordEncoder.encode(rawPassword)); // Aquí usas el encoder correctamente

    RoleEntity rolAdmin = rolRepo.findByNombre("ADMIN");
    Set<RoleEntity> roles = new HashSet<>();
    roles.add(rolAdmin);

    admin.setRoles(roles);

    return userRepo.save(admin);
}

  @Transactional
    public UserEntity crearUsuarioAdmin(String nombre, String rawPassword) {
        // Busca el rol ADMIN o lo crea si no existe
        RoleEntity adminRole = rolRepo.findByNombre("ADMIN");
        if (adminRole == null) {
            adminRole = new RoleEntity();
            adminRole.setNombre("ADMIN");
            rolRepo.save(adminRole);
        }

        UserEntity user = new UserEntity();
        user.setNombre(nombre);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of(adminRole));

        return userRepo.save(user);
    }

public boolean validarCredenciales(String email, String password) {
    if (email == null || password == null) {
        System.out.println("Email o password es null");
        return false;
    }

    Optional<UserEntity> userOpt = userRepo.findByEmail(email.trim());
    if (userOpt.isEmpty()) {
        System.out.println("Usuario no encontrado para email: " + email);
        return false;
    }

    UserEntity user = userOpt.get();
    String hashedPassword = user.getPassword();
    System.out.println("Password hasheado en DB: " + hashedPassword);

    boolean matches = passwordEncoder.matches(password, hashedPassword);
    System.out.println("¿Coincide la contraseña? " + matches);

    return matches;
}


    public UserEntity findByUsername(String username) {
    return userRepo.findByNombre(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + username));
}



    
}






