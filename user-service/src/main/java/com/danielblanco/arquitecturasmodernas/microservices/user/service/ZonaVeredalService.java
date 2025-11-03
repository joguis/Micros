package com.danielblanco.arquitecturasmodernas.microservices.user.service;

import com.danielblanco.arquitecturasmodernas.microservices.user.model.ZonaVeredal;
import com.danielblanco.arquitecturasmodernas.microservices.user.repository.ZonaVeredalRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZonaVeredalService {

    @Autowired
    private ZonaVeredalRepository zonaVeredalRepository;

    public List<ZonaVeredal> findAll() {
        return zonaVeredalRepository.findAll();
    }

    public Optional<ZonaVeredal> findById(Long id) {
        return zonaVeredalRepository.findById(id);
    }

    public ZonaVeredal save(ZonaVeredal zonaVeredal) {
        return zonaVeredalRepository.save(zonaVeredal);
    }
}