package com.danielblanco.arquitecturasmodernas.microservices.user.service;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.User;
import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ZonaVeredalService zonaVeredalService;

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User asociarZonaVeredal(Long userId, Long zonaVeredalId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        ZonaVeredal zonaVeredal = zonaVeredalService.findById(zonaVeredalId)
            .orElseThrow(() -> new RuntimeException("Zona Veredal no encontrada"));
        
        user.setZonaVeredal(zonaVeredal);
        return userRepository.save(user);
    }
}